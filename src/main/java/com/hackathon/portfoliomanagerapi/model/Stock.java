package com.hackathon.portfoliomanagerapi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.portfoliomanagerapi.util.StringDistanceCalculator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "Stocks")
@NoArgsConstructor
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stockId;

    @Column(unique=true, nullable=false)
    private String ticker;

    private String name;

    private Stock(Long stockId) {
        this.stockId = stockId;
    }

    public Stock(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public int hashCode() {
        return stockId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Stock other = (Stock) o;
        return ((Stock) o).stockId.equals(other.stockId);
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class StringDistanceComparator implements Comparator<Stock> {

        String referenceQuery;

        Map<Stock, Integer> scores = new HashMap<>();
        StringDistanceCalculator stringDistanceCalculator = new StringDistanceCalculator();

        public StringDistanceComparator(String referenceQuery) {
            this.referenceQuery = referenceQuery.toLowerCase();
        }

        private int getScore(Stock stock) {
            if(!(scores.containsKey(stock))) {
                int nameScore = stringDistanceCalculator.getDistance(stock.getName().toLowerCase(), referenceQuery);
                int tickerScore = stringDistanceCalculator.getDistance(stock.getTicker().toLowerCase(), referenceQuery);

                int score = Math.min(nameScore, tickerScore);
                scores.put(stock, score);
            }
            return scores.get(stock);
        }

        @Override
        public int compare(Stock stock1, Stock stock2) {
            int score1 = getScore(stock1);
            int score2 = getScore(stock2);

            return score1 - score2;

        }
    }
}
