package com.hackathon.portfoliomanagerapi.util;

import org.springframework.stereotype.Service;

@Service
public class DoubleUtil {

    public double trimTo2DecimalPlaces(double d) {
        int temp = (int)(d*100.0);
        return ((double)temp)/100.0;
    }
}
