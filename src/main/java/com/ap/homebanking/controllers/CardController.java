package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.CardService;
import com.ap.homebanking.services.ClientService;
import com.ap.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,
                                           @RequestParam CardColorType cardColor, Authentication authentication) {
        //el nombre del parámetro debe coincidir con lo que dice create-cards.js que es cardType y cardColor, sino 400 badRequest
        if(cardType == null) {
            return new ResponseEntity<>("Card type (credit, debit) is necessary", HttpStatus.FORBIDDEN);
        } else if (cardColor == null) {
            return new ResponseEntity<>("color type (gold, silver, titanium) is necessary", HttpStatus.FORBIDDEN);
        }

        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient.getCards().stream().filter(typeCard -> typeCard.getType().equals(cardType)).collect(Collectors.toSet()).size() < 3) {
            Card newCard = new Card(currentClient.getFirstName()+" "+currentClient.getLastName(),
                    cardType, cardColor, CardUtils.randomNumberCard(), CardUtils.randomNumberCvv(),
                    LocalDate.now(), LocalDate.now().plusYears(5));

            currentClient.addCard(newCard);
            clientService.saveClient(currentClient);
            cardService.saveCard(newCard);
            return new ResponseEntity<>("Card created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Card limit reached", HttpStatus.FORBIDDEN);
        }
    }

    // task11
//    @PatchMapping("clients/current/cards")
//    public ResponseEntity<Object> deleteCard(Authentication authentication) {
//        Client currentClient = clientService.getClientByEmail(authentication.getName());
//        return currentClient.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
//    }
}
