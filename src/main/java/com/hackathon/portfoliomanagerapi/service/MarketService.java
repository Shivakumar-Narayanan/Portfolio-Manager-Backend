package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Stock;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class MarketService {

    public double getQuote(Stock stock) {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("#.##");
        double rangeMin = 50.0;
        double rangeMax = 400.0;

        double res = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return trimTo2DecimalPlaces(res);
    }

    private double trimTo2DecimalPlaces(double d) {
        int temp = (int)(d*100.0);
        return ((double)temp)/100.0;
    }
}
