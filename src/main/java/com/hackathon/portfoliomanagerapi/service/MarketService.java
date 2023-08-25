package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class MarketService {

    @Autowired
    DoubleUtil doubleUtil;

    @Autowired
    TimeStampGenerator timeStampGenerator;

    public double getQuoteRandom(Stock stock) {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("#.##");
        double rangeMin = 50.0;
        double rangeMax = 400.0;

        double res = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return doubleUtil.trimTo2DecimalPlaces(res);
    }

    public double getQuote(Stock stock) {
        try {
            return doubleUtil.trimTo2DecimalPlaces(getQuote(stock, LocalDate.now()));
        }
        catch(Exception e) {
            System.out.println("could not fetch data");
            return getQuoteRandom(stock);
        }
    }

    public double getQuote(Stock stock, LocalDate date) {
        // somehow fetch reasonable looking data
        try {
            return doubleUtil.trimTo2DecimalPlaces(YahooFinanceAPI.getStockQuote(stock.getTicker(), date));
        }
        catch(Exception e) {
            e.printStackTrace();
            return getQuote(stock);
        }
    }

    public List<Pair<LocalDate, Double>> getStockValueOverTime(String ticker, LocalDate startDate, LocalDate endDate) throws Exception {
//        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);
//
//        List<Pair<LocalDate, Double>> res = new ArrayList<>();
//
//        Stock stock = new Stock(ticker);
//
//        for(LocalDate samplingDate : samplingDates) {
//            System.out.println(samplingDate);
//            double quote = getQuote(stock, samplingDate);
//            res.add(
//                   new Pair<>(samplingDate, quote)
//            );
//        }
//
//        return res;

        List<Pair<LocalDate, Double>> quotes = YahooFinanceAPI.getBulkStockQuotes(ticker);

        return selectUniformItems(quotes, 10);
    }

    public static List<Pair<LocalDate, Double>> selectUniformItems(List<Pair<LocalDate, Double>> items, int itemCount) {
        Collections.reverse(items);
        List<Pair<LocalDate, Double>> selectedItems = new ArrayList<>();
        int totalItems = items.size();
        double segmentSize = (double) totalItems / itemCount;

        for (int i = 0; i < itemCount; i++) {
            int index = (int) Math.floor(i * segmentSize);
            selectedItems.add(items.get(index));
        }
        Collections.reverse(items);
        Collections.reverse(selectedItems);
        return selectedItems;
    }
}
