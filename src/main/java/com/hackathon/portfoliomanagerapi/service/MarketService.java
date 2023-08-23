package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.util.DoubleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Random;

@Service
public class MarketService {

    @Autowired
    DoubleUtil doubleUtil;

    public double getQuote(Stock stock) {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("#.##");
        double rangeMin = 50.0;
        double rangeMax = 400.0;

        double res = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return doubleUtil.trimTo2DecimalPlaces(res);
    }

    public double getQuote(Stock stock, LocalDate date) {
        return getQuote(stock);
    }

}
