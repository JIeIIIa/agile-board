package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.dto.CardDto;
import com.gmail.onishchenko.oleksii.agile.service.CardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The rest controller to process request related to cards information
 */
@RestController
@RequestMapping("/api")
public class CardController {
    private static final Logger log = LogManager.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService) {
        log.info("Create instance of {}", CardController.class);
        this.cardService = cardService;
    }

    /**
     * Find the cards list of the authenticated user
     *
     * @return the card data transfer objects list of the authenticated user
     */
    @GetMapping("/cards")
    public ResponseEntity<List<CardDto>> retrieveAllCards() {
        String login = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        List<CardDto> list = cardService.retrieveAll(login);
        log.debug("Cards were retrieved (login = {})", login);

        return ResponseEntity.ok(list);
    }

    /**
     * Add a new card to the cards list of the authenticated user
     *
     * @return the added card data transfer object
     */
    @PostMapping("/cards")
    public ResponseEntity<CardDto> addCard(@Validated CardDto cardDto) {
        associateWithLogin(cardDto);
        log.debug("Adding card {}", cardDto);
        CardDto addedCard = cardService.add(cardDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addedCard);
    }

    /**
     * Update a card with specified id and that was created by the authenticated user
     *
     * @param id      the card's id
     * @param cardDto a data transfer object that contains new information
     * @return the updated card data transfer object
     */
    @PutMapping("/cards/{id}")
    public ResponseEntity<CardDto> update(@PathVariable("id") Long id, @Validated CardDto cardDto) {
        associateWithLogin(cardDto);
        cardDto.setId(id);
        CardDto updatedCard = cardService.update(cardDto);
        return ResponseEntity.ok(updatedCard);
    }

    /**
     * Delete a card with specified id and that was created by the authenticated user
     *
     * @param id the card's id that should be deleted
     */
    @DeleteMapping("/cards/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        CardDto cardDto = new CardDto();
        associateWithLogin(cardDto);
        cardDto.setId(id);
        cardService.delete(cardDto);
    }

    /**
    * Replace a user's login in a card data transfer object to
     * the current authenticated user's login
    * */
    private void associateWithLogin(CardDto cardDto) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        cardDto.setUserLogin(login);
    }
}
