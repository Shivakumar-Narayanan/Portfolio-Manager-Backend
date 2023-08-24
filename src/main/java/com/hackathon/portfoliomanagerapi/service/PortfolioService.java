package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.*;
import com.hackathon.portfoliomanagerapi.repository.StockSnapshotRepository;
import com.hackathon.portfoliomanagerapi.repository.TransactionRepository;
import com.hackathon.portfoliomanagerapi.util.DoubleUtil;
import com.hackathon.portfoliomanagerapi.util.Pair;
import com.hackathon.portfoliomanagerapi.util.TimeStampGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    TimeStampGenerator timeStampGenerator;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    StockSnapshotRepository stockSnapshotRepository;

    @Autowired
    MarketService marketService;

    @Autowired
    DoubleUtil doubleUtil;

    @Value("${numtopmovers}")
    int numTopMovers;

    public PortfolioSnapshot getPortfolioAsOn(LocalDate date) {
        PortfolioSnapshot res = new PortfolioSnapshot(date);

        List<StockSnapshot> stockSnapshots = stockSnapshotRepository.getPortfolioCompositionAsOn(0L, date);

        stockSnapshots.forEach(
                stockSnapshot -> {
                    stockSnapshot.setCurrentPricePerShare(marketService.getQuote(stockSnapshot.getStock(), date));
                    res.addStockSnapshot(stockSnapshot);
                }

        );

        return res;
    }

    public Double getPortfolioValueAsOn(LocalDate date) {
        PortfolioSnapshot portfolioSnapshot = getPortfolioAsOn(date);

        List<StockSnapshot> stockSnapshots = portfolioSnapshot.getStocksSnapshots();

        double totalValue = stockSnapshots.stream().
                map(stockSnapshot -> stockSnapshot.getQuantity() * marketService.getQuote(stockSnapshot.getStock(), date)).
                reduce(0.0, Double::sum);

        totalValue = doubleUtil.trimTo2DecimalPlaces(totalValue);
        return totalValue;
    }

    public List<PortfolioSnapshot> getPortfolioCompositionOverTime(LocalDate startDate, LocalDate endDate) {

        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);

        List<PortfolioSnapshot> res = new ArrayList<>();

        for(LocalDate samplingDate : samplingDates) {
            res.add(getPortfolioAsOn(samplingDate));
        }

        return res;
    }

    public List<Pair<LocalDate, Double>> getPortfolioValueOverTime(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);

        List<Pair<LocalDate, Double>> res = new ArrayList<>();

        for(LocalDate samplingDate : samplingDates) {
            res.add(new Pair<>(samplingDate, getPortfolioValueAsOn(samplingDate)));
        }

        return res;
    }

    public Double getTotalInvestedAsOn(LocalDate date) {
        return doubleUtil.trimTo2DecimalPlaces(transactionRepository.getTotalInvestedAsOn(0L, date));
    }

    public Double getCurrentProfitOrLoss() {
        LocalDate currentDate = LocalDate.now();
        return doubleUtil.trimTo2DecimalPlaces(getPortfolioValueAsOn(currentDate) - getTotalInvestedAsOn(currentDate));
    }

    public Double getCurrentProfitOrLossPercentage() {
        LocalDate currentDate = LocalDate.now();
        double totalInvestedTillNow = getTotalInvestedAsOn(currentDate);
        if(totalInvestedTillNow == 0) {
            return 0.0;
        }
        return doubleUtil.trimTo2DecimalPlaces((getCurrentProfitOrLoss() * 100) / totalInvestedTillNow);
    }

    public List<StockSnapshot> getTopGainers() {
        return rankStockSnapshots(StockSnapshot::compareTo);
    }

    public List<StockSnapshot> getTopLosers() {
        return rankStockSnapshots(Collections.reverseOrder());
    }

    private List<StockSnapshot> rankStockSnapshots(Comparator<StockSnapshot> comparator) {
        LocalDate currentDate = LocalDate.now();

        PortfolioSnapshot portfolioSnapshot = getPortfolioAsOn(currentDate);
        List<StockSnapshot> stockSnapshots = portfolioSnapshot.getStocksSnapshots();

        stockSnapshots.forEach(stockSnapshot -> {
            stockSnapshot.setCurrentPricePerShare(marketService.getQuote(stockSnapshot.getStock(), currentDate));
        });

        stockSnapshots.sort(comparator);

        return stockSnapshots.stream().limit(numTopMovers).collect(Collectors.toList());
    }

    private List<StockSnapshot> augmentStocksWithPrice(List<Stock> stocks, LocalDate date) {
        return stocks.stream().
                map(stock -> new StockSnapshot(stock, marketService.getQuote(stock, date))).
                collect(Collectors.toList());
    }

    public String wrapDoubleInJson(Double d) {
        return "{ \"value\": " + d + "}";
    }
}
