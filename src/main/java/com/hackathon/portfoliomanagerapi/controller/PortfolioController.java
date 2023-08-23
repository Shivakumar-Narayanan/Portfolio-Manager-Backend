package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.model.PortfolioSnapshot;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.model.StockSnapshot;
import com.hackathon.portfoliomanagerapi.service.PortfolioService;
import com.hackathon.portfoliomanagerapi.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;

    @GetMapping("/currentPortfolio")
    public ResponseEntity<PortfolioSnapshot> getCurrentPortfolioComposition() {
        try {
            LocalDate currentDate = LocalDate.now();
            return ResponseEntity.ok(portfolioService.getPortfolioAsOn(currentDate));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/portfolioAsOn")
    public ResponseEntity<PortfolioSnapshot> getPortfolioCompositionAsOn(@DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        try {
            return ResponseEntity.ok(portfolioService.getPortfolioAsOn(date));
        }
        catch(Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/portfolioValueOverTime")
    public ResponseEntity<List<Pair<LocalDate, Double>>> getPortfolioValueOverTime(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate) {
        try {
            return ResponseEntity.ok().body(portfolioService.getPortfolioValueOverTime(startDate, endDate));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/topGainers")
    public ResponseEntity<List<StockSnapshot>> getTopGainers() {
        try {
            return ResponseEntity.ok(portfolioService.getTopGainers());
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/topLosers")
    public ResponseEntity<List<StockSnapshot>> getTopLosers() {
        try {
            return ResponseEntity.ok(portfolioService.getTopLosers());
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/currentInvested")
    public ResponseEntity<Double> getCurrentInvested() {
        try {
            LocalDate currentDate = LocalDate.now();
            return ResponseEntity.ok(portfolioService.getTotalInvestedAsOn(currentDate));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(0.0);
        }
    }

    @GetMapping("/currentTotalAmount")
    public ResponseEntity<Double> getCurrentTotalAmount() {
        try {
            LocalDate currentDate = LocalDate.now();
            return ResponseEntity.ok(portfolioService.getPortfolioValueAsOn(currentDate));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(0.0);
        }
    }

    @GetMapping("/profitLoss")
    public ResponseEntity<Double> getProfitLoss() {
        try {
            return ResponseEntity.ok(portfolioService.getCurrentProfitOrLoss());
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(0.0);
        }
    }

    @GetMapping("/profitLossPercentage")
    public ResponseEntity<Double> getProfitLossPercentage() {
        try {
            return ResponseEntity.ok(portfolioService.getCurrentProfitOrLossPercentage());
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(0.0);
        }
    }
}
