package com.hackathon.portfoliomanagerapi.util;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DateFormatConverter {
    public String convert(String inputDate, String inputFormat, String outputFormat) {

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

            return date.format(outputFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            return inputDate;
        }
    }
}

