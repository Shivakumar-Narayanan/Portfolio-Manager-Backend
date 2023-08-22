package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/addTransaction")
    public ResponseEntity<String> addSecurityToPortfolio(@RequestBody Transaction transaction) {
        System.out.println("Trying to add new Transaction: " + transaction);

        try {
            transactionService.addTransaction(transaction);
            return ResponseEntity.ok("transaction created");
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not add transaction");
        }
    }

}
