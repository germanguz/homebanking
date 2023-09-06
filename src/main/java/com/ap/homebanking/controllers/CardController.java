package com.ap.homebanking.controllers;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.CardService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

//    @Autowired
//    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;

//    @Autowired
//    private CardRepository cardRepository;
    @Autowired
    private CardService cardService;

    // Funciones para crear los números random de la tarjeta y cvv
    public String randomNumberCard() {
        int numberA = 0;
        int numberB = 0;
        char hyphen = '-';
        String cardNumber = "";
        do {
            numberA = (int)(Math.random()*100000000);
            numberB = (int)(Math.random()*100000000);
        } while (numberA < 10000000 || numberB < 10000000);
        String partNumberA = Integer.toString(numberA);
        String partNumberB = Integer.toString(numberB);
        cardNumber = partNumberA.substring(0,4)+hyphen+partNumberA.substring(4)+hyphen+
                partNumberB.substring(0,4)+hyphen+partNumberB.substring(4);
        return cardNumber;
    }

    public int randomNumberCvv() {
        return (int)((Math.random()*(1000 - 100)) + 99); //sumo 99 para evitar el posible 1000
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,
                                           @RequestParam CardColorType cardColor, Authentication authentication) {
        //el nombre del parámetro debe coincidir con lo que dice create-cards.js que es cardType y cardColor, sino 400 badRequest
        if(cardType == null) {
            return new ResponseEntity<>("Card type (credit, debit) is necessary", HttpStatus.FORBIDDEN);
        } else if (cardColor == null) {
            return new ResponseEntity<>("color type (gold, silver, titanium) is necessary", HttpStatus.FORBIDDEN);
        }

        //Client currentClient = clientRepository.findByEmail(authentication.getName());
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient.getCards().stream().filter(typeCard -> typeCard.getType().equals(cardType)).collect(Collectors.toSet()).size() < 3) {
            Card newCard = new Card(currentClient.getFirstName()+" "+currentClient.getLastName(),
                    cardType, cardColor, randomNumberCard(), randomNumberCvv(),
                    LocalDate.now(), LocalDate.now().plusYears(5));

            currentClient.addCard(newCard);
            //clientRepository.save(currentClient);
            clientService.saveClient(currentClient);
            //cardRepository.save(newCard);
            cardService.saveCard(newCard);
            return new ResponseEntity<>("Card created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Card limit reached", HttpStatus.FORBIDDEN);
        }
    }
}
