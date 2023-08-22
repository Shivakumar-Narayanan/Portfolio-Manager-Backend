package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.exceptions.StockExistsException;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    StockService StockService;

    @PostMapping("/addStock")
    public ResponseEntity<String> addStock(@RequestBody Stock stock) {
        try {
            System.out.println("Request to create Stock: " + stock);
            StockService.addStock(stock);
            return ResponseEntity.ok("Stock Added");
        }
        catch(StockExistsException stockExistsException) {
            return ResponseEntity.ok("Stock Already Exists");
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not add Stock");
        }
    }
}
