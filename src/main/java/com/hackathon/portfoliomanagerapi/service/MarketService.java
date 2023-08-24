package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.util.DoubleUtil;
import com.hackathon.portfoliomanagerapi.util.Pair;
import com.hackathon.portfoliomanagerapi.util.TimeStampGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MarketService {

    @Autowired
    DoubleUtil doubleUtil;

    @Autowired
    TimeStampGenerator timeStampGenerator;

    public double getQuote(Stock stock) {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("#.##");
        double rangeMin = 50.0;
        double rangeMax = 400.0;

        double res = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return doubleUtil.trimTo2DecimalPlaces(res);
    }

    public double getQuote(Stock stock, LocalDate date) {
        // somehow fetch reasonable looking data
        return getQuote(stock);
    }

    public List<Pair<LocalDate, Double>> getStockValueOverTime(String ticker, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);

        List<Pair<LocalDate, Double>> res = new ArrayList<>();

        Stock stock = new Stock(ticker);

        for(LocalDate samplingDate : samplingDates) {
            double quote = getQuote(stock, samplingDate);
            res.add(
                   new Pair<>(samplingDate, quote)
            );
        }

        return res;
    }
}
