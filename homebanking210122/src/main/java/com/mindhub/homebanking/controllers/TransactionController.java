package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;


    /*
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transferir(@RequestParam String fromAccountNumber,
                                             @RequestParam String toAccountNumber, @RequestParam double amount,
                                             @RequestParam String description, Authentication config) {


        Client clienteAuth = clientRepository.findByEmail(config.getName());

        if (clienteAuth != null) { //Si es un cliente autentificado
            if (fromAccountNumber != null && toAccountNumber != null && amount != 0 && description != null) {
                //Validar que params no vacios

                Account cOrigen = accountRepository.findAccountByNumber(fromAccountNumber);
                Account cDestino = accountRepository.findAccountByNumber(toAccountNumber);

                if (cOrigen != null && cDestino != null) {
                    //Validar que existen
                    if (cOrigen.getId() != cDestino.getId()) {
                        //Validar que no son la misma


                        //Validar que cliente sea el propietario de cuenta origen
                        boolean dueñoCuenta=false;
                       // ArrayList cuentasCliente= (ArrayList) clienteAuth.getAccounts();
                      /* for(int i=0;i<= clienteAuth.getAccounts().size();i++){
                            Account checkAcc= (Account) clienteAuth.getAccounts().stream().

                            if(checkAcc.getId() ==cOrigen.getId()){
                                dueñoCuenta=true;
                                break;
                            }
                       }*/
                    /*    Set<Account> cuentas = clienteAuth.getAccounts();

                        Iterator iter = cuentas.iterator();
                        while (iter.hasNext()){
                            Account cuenta = (Account) iter.next();
                            if (cuenta.getId() == cOrigen.getId()){
                                dueñoCuenta=true;
                                break;
                            }}

                       if(dueñoCuenta) {

                           if (cOrigen.getBalance()> amount) {
                             //Validar si hay balance para la trasacción


                               //INICIO TRANSACCION
                               //Instaciar las transacciones y sus características
                               Transaction transactionOrigen = new Transaction(TransactionType.DEBIT, amount * -1, description + "  " + cDestino.getNumber(), LocalDateTime.now(), cOrigen);
                               Transaction transactionDestino = new Transaction(TransactionType.CREDIT, amount, description + "  " + cOrigen.getNumber(), LocalDateTime.now(), cDestino);

                               transactionRepository.save(transactionOrigen);
                               transactionRepository.save(transactionDestino);

                               //Modificar saldo cuentas

                               cOrigen.setBalance(cOrigen.getBalance() - amount);
                               cDestino.setBalance(cDestino.getBalance() + amount);
                               // accountRepository.save(cOrigen);
                               // accountRepository.save(cDestino);

                               return new ResponseEntity<>("Transación hecha con exito", HttpStatus.CREATED);

                           }else{
                               return new ResponseEntity<>("No hay monto sificiente para esta transacción.", HttpStatus.FORBIDDEN);
                           }

                       }else{
                           return new ResponseEntity<>("Usted no es dueño de la cuenta de origen.", HttpStatus.FORBIDDEN);
                       }
                    } else {
                        return new ResponseEntity<>("No puedes trasferir a tu misma cuenta", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>("Datos mal ingresados. Una de las cuentas no existe.", HttpStatus.FORBIDDEN);
                }


            }else{
                return new ResponseEntity<>("Necesita ingresar todos los datos.", HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<>("Debe estar autentificado para esta operación.", HttpStatus.FORBIDDEN);
        }
    }

*/


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> doTransaction(@RequestParam String accountFromNumber,
                                                @RequestParam String accountToNumber,
                                                @RequestParam double amount,
                                                @RequestParam String description,
                                                Authentication authentication){
        //VERIFICACION DE PARAMETROS
        if (accountFromNumber == null || accountToNumber == null || amount == 0) {
            return new ResponseEntity<>("Parametros invalidos", HttpStatus.FORBIDDEN);
        }
        //VERIFICANDO EXISTENCIA DE CUENTAS
        if(accountRepository.findByNumber(accountFromNumber) == null) {
            return new ResponseEntity<>("La cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }
            if (accountRepository.findByNumber(accountFromNumber) == null) {
                return new ResponseEntity<>("La cuenta de destiono no exite", HttpStatus.FORBIDDEN);
            }
            //VERIFICAR IGUALDAD CUENTA ORIGEN-DESTINO
            if (accountFromNumber.equals(accountToNumber)) {
                return new ResponseEntity<>("Las cuentas de origen y destino son las mismas!", HttpStatus.FORBIDDEN);
            }
            //VERIFICAR PERTENENCIA DE LA CUENTA DE ORIGEN
            Client client = clientRepository.findByEmail(authentication.getName());
            Set<Account> accounts = client.getAccounts();
            Iterator iter = accounts.iterator();
            Boolean found = false;
            Account srcAccount = null;
            while (iter.hasNext()) {
                Account account = (Account) iter.next();
                if (account.getNumber().equals(accountFromNumber)) {
                    found = true;
                    //GUARDAR CUENTA
                    srcAccount = account;
                    break;
                }
            }
            if (!found) {
                return new ResponseEntity<>("El cliente auntentificado no es dueño de la cuenta de origen!", HttpStatus.FORBIDDEN);
            }

            //VERIFICACION DE SALDO
            if (srcAccount.getBalance() < amount) {
                return new ResponseEntity<>("Saldo insuficiente para realizar la operacion", HttpStatus.FORBIDDEN);
            }

            //TRANSACCION VALIDA
            Account destAccount = accountRepository.findByNumber(accountToNumber);

            Transaction origenTransaction = new Transaction(TransactionType.DEBIT, -amount, description, LocalDateTime.now(), srcAccount);
            Transaction destinoTransaction = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), destAccount);
            transactionRepository.save(origenTransaction);
            transactionRepository.save(destinoTransaction);

            srcAccount.setBalance(srcAccount.getBalance() - amount);
            destAccount.setBalance(destAccount.getBalance() + amount);

            return new ResponseEntity<>("Operación completada con éxito", HttpStatus.ACCEPTED);

        }


}
