package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
            private TransactionRepository transactionRepository;



// VARIABLES DE TIPO LOAN
    /*
    Loan loanH=new Loan("Hipotecario", 500000, Arrays.asList(12,24,36,48,60));
    Loan loanP=new Loan("Personal", 100000, Arrays.asList(6,4,8,10,20));
    Loan loanA=new Loan("Automotriz", 300000, Arrays.asList(12,24,36,48,60));
*/
    @GetMapping("/loans")
    public List<LoanDTO> listarLoans(Authentication authentication){
        return this.loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }



    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> aplicarLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client=clientRepository.findByEmail(authentication.getName());
        //Si cliente está autentificado
        if(client!=null){

            //Validar que campos no etsén vacios o =0
            if (loanApplicationDTO.getAmount()!=0 && loanApplicationDTO.getPayments()!=0 && loanApplicationDTO.getToAccountNumber()!=null){

                //Validar que el tipo de loan exista

       // Loan loan=loanRepository.findByName(String.valueOf(loanApplicationDTO.getType()));
                Optional<Loan> loan = loanRepository.findById(loanApplicationDTO.getLoanId());
                    //loanC.get().
                if(loan!=null){
                    //Loan loan=loanRepository.findByType(loanApplicationDTO.getType());

                  //Validar que cuotas sean correspondientes a su tipo
                  boolean cuotasValida=false;
                  for (int x:loan.get().getPayments().stream().toList()){
                      if(loanApplicationDTO.getPayments()==x){
                          cuotasValida=true;
                          break;

                      }
                  }
                      //Pregunto si son validas el num cuotas
                      if(cuotasValida){


                          //validar el monto maximo del prestamo
                          if(loan.get().getMaxAmount()>=loanApplicationDTO.getAmount()){

                            //Validar cuenta destino exista
                              Account accountDestino=accountRepository.findAccountByNumber(loanApplicationDTO.getToAccountNumber());
                              if(accountDestino!=null){
                                  //validar que le pertenezca al cliennte autentificado
                                  if(accountDestino.getClient().getId()==client.getId()){

                                      //Inicio trasaccion

                                      //Creo mi trasacción
                                      double amount=loanApplicationDTO.getAmount()+(loanApplicationDTO.getAmount()*0.2);
                                      Transaction transaction=new Transaction(TransactionType.CREDIT,amount,loan.get().getName()+": Prestamo aprovado.", LocalDateTime.now(),accountDestino);
                                        //Actualizar baalance ccuenat destino
                                      accountDestino.setBalance(accountDestino.getBalance()+amount);
                                      transactionRepository.save(transaction);
                                        loanRepository.save(loan.get());
                                      return new ResponseEntity<>("Prestamo realizado", HttpStatus.ACCEPTED);






                                  }else{
                                      return new ResponseEntity<>("No puede solictar prestamo por alguien más.", HttpStatus.FORBIDDEN);
                                  }


                              }else{
                                  return new ResponseEntity<>("La cuenta de destino no existe.", HttpStatus.FORBIDDEN);
                              }


                          }else{
                              return new ResponseEntity<>("Está solicitando más dinero del permitido para este prestamo.", HttpStatus.FORBIDDEN);
                          }

                      }else{
                          return new ResponseEntity<>("No está permitido está cantidad de cuotas para este prestamo.", HttpStatus.FORBIDDEN);
                      }

                  }else{
                  return new ResponseEntity<>("No existe este tipo de prestamo.", HttpStatus.FORBIDDEN);
              }

            }else{
                return new ResponseEntity<>("Debe ingresar toddos los campos para la solicitud.", HttpStatus.FORBIDDEN);
            }
        }else {
            return new ResponseEntity<>("Debe estar autentificado.", HttpStatus.FORBIDDEN);
        }


    }


}
