package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;


    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> agregarTarjeta(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication config){

            Client c = clientRepository.findByEmail(config.getName());


            if (c.getCards().size() < 3 && c!=null) {
                Random random = new Random();
                String numberCVV = String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9));

                //GENERAR NUM CUENTA AUTO y UNICO
                String number = " ";
                int validacionNum = 0;
                do {
                    String num = "";
                    for (int i = 1; i <= 4; i++) {
                        //Generar bloques de num
                        for (int a = 1; a <= 4; a++) {
                            //Generar 4 numeros del bloque
                            int ranNum = random.nextInt(0, 9);
                            num = num + String.valueOf(ranNum);
                        }
                        //agergao el guion despues del bloque e itero
                        num = num + "-";
                    }

                    //chequeo que num no exista
                    Card cardCheck = cardRepository.findByNumber(num);
                    if (cardCheck == null) {
                        number = num;
                        validacionNum = 1;
                    }
                } while (validacionNum == 0);

                Card card = new Card(c.getFirstName() + " " + c.getLastName(), cardType, cardColor, number, Integer.parseInt(numberCVV), LocalDateTime.now().plusYears(5), LocalDateTime.now(), c);
                cardRepository.save(card);
                return new ResponseEntity<Object>(HttpStatus.CREATED);

            } else {
                return new ResponseEntity<Object>("Ya tiene 3 tarjetas", HttpStatus.FORBIDDEN);
            }


    }


}
