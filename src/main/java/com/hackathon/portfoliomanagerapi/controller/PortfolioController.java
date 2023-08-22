package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.service.PortfolioService;
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

    @GetMapping("/portfolioValueOverTime")
    public ResponseEntity<Map<LocalDate, Map<Stock, Integer>>> getPortfolioValueOverTime(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate) {
        try {
            return ResponseEntity.ok().body(portfolioService.getPortfolioCompositionOverTime(startDate, endDate));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
