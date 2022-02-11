package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class AccountController {


    @Autowired
private ClientRepository clientRepository;



    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getClients(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
/*
    @RequestMapping("/accounts/{id}")
    public AccountDTO getClient(@PathVariable Long id){
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
*/

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Set<Account> cuentas = client.getAccounts();

        Iterator iter = cuentas.iterator();
        while (iter.hasNext()){
            Account cuenta = (Account) iter.next();
            if (cuenta.getId() == id){
                return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
            }
        }
        return null;
    }


    //@RequestMapping(path="/client/current/account", method = RequestMethod.POST)
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Client c = clientRepository.findByEmail(authentication.getName());

        if (c.getAccounts().size() < 3) {
            Account account = new Account();
            account.setBalance(0);
            account.setCreationDate(LocalDateTime.now());
            account.setClient(c);

            //generar numero cuenta
            int validacionNum = 0;
            do {
                String num = "VIN-";
                for (int i = 1; i <= 8; i++) {
                    Random random=new Random();
                  int ranNum = random.nextInt(0,9);

                  num = num + String.valueOf(ranNum);
                }



                Account accountCheck = accountRepository.findAccountByNumber(num);
                if (accountCheck == null) {
                    account.setNumber(num);
                    validacionNum = 1;

                }
            } while (validacionNum == 0);

            accountRepository.save(account);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Ya tiene 3 cuentas.", HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }




/*
    @RequestMapping("/clients/current/findaccount")
    public Account findAccount(String num){
        return accountRepository.findAccountByNumber(num);
    }
*/
}
