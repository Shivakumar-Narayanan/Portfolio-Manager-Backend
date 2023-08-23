package com.hackathon.portfoliomanagerapi.model;

import com.hackathon.portfoliomanagerapi.util.Pair;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class PortfolioSnapshot {
    Pair<LocalDate, List<Stock>> portfolio;

    public PortfolioSnapshot(LocalDate date) {
        this.portfolio = new Pair<>(date, new ArrayList<>());
    }

    public void addStock(Stock stock) {
        this.portfolio.getValue().add(stock);
    }

    public LocalDate getDate() {
        return portfolio.getKey();
    }

    public List<Stock> getStocks() {
        return portfolio.getValue();
    }

    public void sortPortfolio(Comparator<Stock> comparator) {
        this.portfolio.getValue().sort(comparator);
    }
}

