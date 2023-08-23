package com.hackathon.portfoliomanagerapi.model;

import com.hackathon.portfoliomanagerapi.util.Pair;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class PortfolioSnapshot {
    Pair<LocalDate, List<StockSnapshot>> portfolio;

    public PortfolioSnapshot(LocalDate date) {
        this.portfolio = new Pair<>(date, new ArrayList<>());
    }

    public void addStockSnapshot(StockSnapshot stockSnapshot) {
        this.getStocksSnapshots().add(stockSnapshot);
    }

    public LocalDate getDate() {
        return portfolio.getKey();
    }

    public List<StockSnapshot> getStocksSnapshots() {
        return portfolio.getValue();
    }

    public void sortPortfolio(Comparator<StockSnapshot> comparator) {
        this.portfolio.getValue().sort(comparator);
    }
}

