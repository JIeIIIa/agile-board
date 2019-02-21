package com.gmail.onishchenko.oleksii.agile.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.gmail.onishchenko.oleksii.agile.configuration.CryptConfiguration;
import com.gmail.onishchenko.oleksii.agile.configuration.DBUnitConfiguration;
import com.gmail.onishchenko.oleksii.agile.dto.CardDto;
import com.gmail.onishchenko.oleksii.agile.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;
import org.dbunit.database.IDatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {
        DBUnitConfiguration.class,
        CryptConfiguration.class
})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup(value = "classpath:datasets/card/init_dataset.xml")
@Transactional
@ActiveProfiles({"test", "crypt"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CardServiceImplIT {

    @Autowired
    private IDatabaseConnection connection;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CardServiceImpl instance;

    @BeforeEach
    void setUp() throws SQLException {
        Statement statement = connection.getConnection().createStatement();
        statement.execute("alter sequence userinfo_id_seq RESTART WITH 1");
        statement.execute("alter sequence card_id_seq RESTART WITH 1");
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void retrieveAllWhenUserExists() {
        //Given
        final String login = "Admin";

        CardDto first = new CardDto();
        first.setId(-1001L);
        first.setStatus("To do");
        first.setText("First task");
        first.setUserLogin(login);
        CardDto second = new CardDto();
        second.setId(-1002L);
        second.setStatus("In progress");
        second.setText("Second task");
        second.setUserLogin(login);
        CardDto third = new CardDto();
        third.setId(-1003L);
        third.setStatus("Done");
        third.setText("Third task");
        third.setUserLogin(login);

        CardDto[] expected = {first, second, third};

        //When
        instance.retrieveAll(login);

        //Then
        assertThat(expected).containsExactlyInAnyOrder(expected);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void retrieveAllWhenUserNotExists() {
        //When
        assertThatThrownBy(() -> instance.retrieveAll("fake-user"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/added_dataset.xml")
    @Test
    void addSuccess() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setStatus("Done");
        cardDto.setText("Awesome task");
        cardDto.setUserLogin("Admin");
        CardDto expected = new CardDto();
        expected.setId(1L);
        expected.setStatus("Done");
        expected.setText("Awesome task");
        expected.setUserLogin("Admin");

        //When
        CardDto result = instance.add(cardDto);

        //Then
        assertThat(result).isEqualTo(expected);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void addWhenUserNotExists() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setStatus("Done");
        cardDto.setText("text");
        cardDto.setUserLogin("fake-user");

        //When
        assertThatThrownBy(() -> instance.add(cardDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/updated_dataset.xml")
    @Test
    void updateSuccess() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setId(-1003L);
        cardDto.setStatus("Done");
        cardDto.setText("Finished task");
        cardDto.setUserLogin("Admin");
        CardDto expected = new CardDto();
        expected.setId(-1003L);
        expected.setStatus("Done");
        expected.setText("Finished task");
        expected.setUserLogin("Admin");

        //When
        CardDto result = instance.update(cardDto);

        //Then
        assertThat(result).isEqualTo(expected);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void updateWhenUserNotExists() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setId(111L);
        cardDto.setStatus("Done");
        cardDto.setText("text");
        cardDto.setUserLogin("fake-user");

        //When
        assertThatThrownBy(() -> instance.update(cardDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void updateWhenCardNotExists() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setId(111L);
        cardDto.setStatus("Done");
        cardDto.setText("text");
        cardDto.setUserLogin("Admin");

        //When
        assertThatThrownBy(() -> instance.update(cardDto))
                .isInstanceOf(CardNotFoundException.class);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/deleted_dataset.xml")
    @Test
    void deleteSuccess() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setId(-1003L);
        cardDto.setUserLogin("Admin");

        //When
        instance.delete(cardDto);

        //Then
        entityManager.flush();
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void deletedWhenUserNotExists() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setId(-1001L);
        cardDto.setUserLogin("fake-user");

        //When
        assertThatThrownBy(() -> instance.delete(cardDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/card/init_dataset.xml")
    @Test
    void deleteWhenCardNotExists() {
        //Given
        CardDto cardDto = new CardDto();
        cardDto.setId(111L);
        cardDto.setUserLogin("Admin");

        //When
        instance.delete(cardDto);
    }
}