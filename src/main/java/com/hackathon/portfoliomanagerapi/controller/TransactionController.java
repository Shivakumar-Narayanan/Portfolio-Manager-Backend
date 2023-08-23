package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.exceptions.stock.InvalidStockException;
import com.hackathon.portfoliomanagerapi.exceptions.transaction.*;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/addTransaction")
    public ResponseEntity<String> addStockToPortfolio(@RequestBody Transaction transaction) {
        System.out.println("Trying to add new Transaction: " + transaction);

        try {
            transactionService.addTransaction(transaction);
            return ResponseEntity.ok("transaction created");
        }
        catch(InvalidStockException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("invalid stock, please provide either valid stock id or valid ticker");
        }
        catch(InvalidTransactionDateException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid Transaction date, please enter a date after ipo and before tomorrow");
        }
        catch(InvalidTransactionException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid Transaction, you don't have enough of this stock to sell");
        }
        catch(InvalidTransactionTypeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid Transaction Type, transaction type should be BUY or SELL");
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not add transaction");
        }
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/deleteTransaction")
    public ResponseEntity<String> deleteTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.deleteTransaction(transaction);
            return ResponseEntity.ok("Transaction Deleted");
        }
        catch(InvalidTransactionDeleteException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid Transaction Delete. This delete would leave your portfolio in an invalid state");
        }
        catch(TransactionDoesntExistException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid Transaction, Please provide a valid TransactionId");
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not delete transaction");
        }
    }

}
