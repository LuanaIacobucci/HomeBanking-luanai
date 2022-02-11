package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return this.clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return this.clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }


    //METODO DE PRUEBA
    @GetMapping("/clients/find/{name}")
    public List<ClientDTO> getListClientsName(@PathVariable String name){
        return  this.clientRepository.findByFirstNameContainingIgnoreCaseOrderByLastName(name).stream().map(ClientDTO::new).collect(toList());
    }


    //Registrar cliente
    @PostMapping(path="/clients")
    public ResponseEntity<Object> registar(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client c=new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(c);

        Account account=new Account(numCuentaAletorio(), LocalDateTime.now(),0,c);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

@GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client c=clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(c);

    }


    public String numCuentaAletorio(){
        String num;
        //generar numero cuenta
        int validacionNum = 0;
        do {
            num = "VIN-";
            for (int i = 1; i <= 8; i++) {
                Random random=new Random();
                int ranNum = random.nextInt(0,9);

                num = num + String.valueOf(ranNum);
            }

            Account accountCheck = accountRepository.findAccountByNumber(num);
            if (accountCheck == null) {
                //account.setNumber(num);
                validacionNum = 1;

            }
        } while (validacionNum == 0);


        return num;
    }




}



