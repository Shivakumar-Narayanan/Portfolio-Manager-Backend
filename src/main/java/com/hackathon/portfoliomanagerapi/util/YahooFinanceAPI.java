package com.hackathon.portfoliomanagerapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YahooFinanceAPI {

    public static Double getStockQuote(String tickerSymbol) throws Exception {
        List<Pair<LocalDate, Double>> bulkQuotes = getBulkStockQuotes(tickerSymbol);

        return getStockQuote(tickerSymbol, LocalDate.now(), bulkQuotes);
    }

    public static Double getStockQuote(String tickerSymbol, LocalDate date, List<Pair<LocalDate, Double>> bulkQuotes) {
        long closestInterval = 100000;
        double priceAtClosestDate = 0.0;

        int i = 0;
        int j = bulkQuotes.size() - 1;

        while(i <= j) {
            LocalDate lo = bulkQuotes.get(i).getKey();
            LocalDate hi = bulkQuotes.get(j).getKey();

            int k = i + (j - i) / 2;

            LocalDate mid = bulkQuotes.get(k).getKey();

            long gap = ChronoUnit.DAYS.between(mid, date);
            if(gap < closestInterval) {
                closestInterval = gap;
                priceAtClosestDate = bulkQuotes.get(k).getValue();
            }

            if(mid.isEqual(date)) {
                break;
            }
            if(mid.isBefore(date)) {
                i = k + 1;
            }
            else {
                j = k - 1;
            }
        }

        Random r = new Random();
        int swing = 40;
        if(r.nextDouble() < 0.5) {
            return priceAtClosestDate - swing * new Random().nextDouble();
        }
        else {
            return priceAtClosestDate + swing * new Random().nextDouble();
        }
    }

    public static LocalDate getMiddleDate(LocalDate date1, LocalDate date2) {
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
        long middleDays = daysBetween / 2;

        return date1.plusDays(middleDays);
    }

    private static double getNoise(double val) {
        Random random = new Random();

        double randomPercentage = generateRandomPercentageInRange(-0.002, 0.02);
        double randomValue = calculateRandomValue(val, randomPercentage);

        return val;
    }

    public static double generateRandomPercentageInRange(double minPercentage, double maxPercentage) {
        Random random = new Random();
        return minPercentage + (maxPercentage - minPercentage) * random.nextDouble();
    }

    public static double calculateRandomValue(double givenValue, double randomPercentage) {
        return givenValue * randomPercentage;
    }

    public static Double getStockQuote(String tickerSymbol, LocalDate date) throws Exception {
        List<Pair<LocalDate, Double>> bulkQuotes = getBulkStockQuotes(tickerSymbol);

        return getStockQuote(tickerSymbol, date, bulkQuotes);
    }

    public static List<Pair<LocalDate, Double>> getBulkStockQuotes(String tickerSymbol) throws Exception {
        String url = "http://localhost:5000/priceHistory?symbol=" + tickerSymbol;

        URL yahooApiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) yahooApiUrl.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        return parseApiOutput(response.toString());
    }

    public static List<Pair<LocalDate, Double>> parseApiOutput(String jsonString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArrayNode = objectMapper.readTree(jsonString);

        List<Pair<LocalDate, Double>> res = new ArrayList<>();

        if (jsonArrayNode.isArray()) {
            for (JsonNode objectNode : jsonArrayNode) {
                String x = objectNode.get("x").asText();
                x = new DateFormatConverter().convert(x, "dd-MM-yyyy", "yyyy-MM-dd");
                double y = objectNode.get("y").asDouble();

                String dateFormat = "yyyy-MM-dd";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                LocalDate localDate = LocalDate.parse(x, formatter);

                res.add(new Pair<>(localDate, y));
            }
        }

        return res;
    }
}
