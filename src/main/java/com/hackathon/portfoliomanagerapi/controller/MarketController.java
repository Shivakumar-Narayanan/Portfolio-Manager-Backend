package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.service.MarketService;
import com.hackathon.portfoliomanagerapi.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/market")
@CrossOrigin
public class MarketController {
    @Autowired
    MarketService marketService;

    @GetMapping("/stockValueOverTime")
    public ResponseEntity<List<Pair<LocalDate, Double>>> stockValueOverTime(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
            @RequestParam String ticker
    ) {

        try {
            return ResponseEntity.ok(marketService.getStockValueOverTime(ticker, startDate, endDate));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
