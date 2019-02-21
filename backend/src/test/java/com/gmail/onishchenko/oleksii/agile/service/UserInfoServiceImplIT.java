package com.gmail.onishchenko.oleksii.agile.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.gmail.onishchenko.oleksii.agile.configuration.CryptConfiguration;
import com.gmail.onishchenko.oleksii.agile.configuration.DBUnitConfiguration;
import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import com.gmail.onishchenko.oleksii.agile.exception.UserAlreadyExistsException;
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
@DatabaseSetup(value = "classpath:datasets/userinfo/init_dataset.xml")
@Transactional
@ActiveProfiles({"test", "crypt"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserInfoServiceImplIT {

    @Autowired
    private IDatabaseConnection connection;

    @Autowired
    private UserInfoService instance;

    @BeforeEach
    void setUp() throws SQLException {
        Statement statement = connection.getConnection().createStatement();
        statement.execute("alter sequence userinfo_id_seq RESTART WITH 1");
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/userinfo/init_dataset.xml")
    @Test
    void addWhenUserAlreadyExists() {
        //Given
        UserInfo userInfo = new UserInfo("Admin", "new-password");
        UserInfoDto userInfoDto = new UserInfoDto(userInfo);

        //When
        assertThatThrownBy(() -> instance.add(userInfoDto))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/userinfo/added_user_dataset.xml")
    @Test
    void addSuccess() {
        //Given
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setLogin("new-user");
        userInfoDto.setPassword("qwerty");

        UserInfoDto expected = new UserInfoDto();
        expected.setLogin("new-user");

        //When
        UserInfoDto result = instance.add(userInfoDto);

        //Then
        assertThat(result).isEqualToComparingFieldByField(expected);
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/userinfo/init_dataset.xml")
    @Test
    void existsSuccess() {
        //When
        boolean result = instance.existsByLogin("token-man");

        //Then
        assertThat(result).isTrue();
    }

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            value = "classpath:datasets/userinfo/init_dataset.xml")
    @Test
    void existsFailure() {
        //When
        boolean result = instance.existsByLogin("fake-man");

        //Then
        assertThat(result).isFalse();
    }
}