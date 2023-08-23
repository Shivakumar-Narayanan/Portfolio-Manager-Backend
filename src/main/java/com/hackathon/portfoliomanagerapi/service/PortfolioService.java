package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.PortfolioSnapshot;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.model.User;
import com.hackathon.portfoliomanagerapi.repository.TransactionRepository;
import com.hackathon.portfoliomanagerapi.util.DoubleUtil;
import com.hackathon.portfoliomanagerapi.util.Pair;
import com.hackathon.portfoliomanagerapi.util.TimeStampGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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
    MarketService marketService;

    @Autowired
    DoubleUtil doubleUtil;

    public List<PortfolioSnapshot> getPortfolioCompositionOverTime(LocalDate startDate, LocalDate endDate) {

        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);

        List<Transaction> buyTransactions = transactionRepository.findByUser(0L, endDate, Transaction.BUY);
        List<Transaction> sellTransactions = transactionRepository.findByUser(0L, endDate, Transaction.SELL);

        List<PortfolioSnapshot> res = new ArrayList<>();

        for(LocalDate samplingDate : samplingDates) {
            res.add(getPortfolioAsOn(samplingDate));
        }

        return res;
    }

    public List<Pair<LocalDate, Double>> getPortfolioValueOverTime(LocalDate startDate, LocalDate endDate) {
        List<Pair<LocalDate, Double>> res = new ArrayList<>();

        List<PortfolioSnapshot> portfolioSnapshots = getPortfolioCompositionOverTime(startDate, endDate);

        portfolioSnapshots.forEach(
                portfolioSnapshot -> {
                    LocalDate date = portfolioSnapshot.getDate();
                    List<Stock> stocks = portfolioSnapshot.getStocks();

                    double totalValue = stocks.stream().
                            map(stock -> stock.getPrice() * stock.getQuantity()).
                            reduce(0.0, Double::sum);

                    totalValue = doubleUtil.trimTo2DecimalPlaces(totalValue);

                    res.add(new Pair<>(date, totalValue));
                }
        );

        return res;
    }

    public PortfolioSnapshot getPortfolioAsOn(LocalDate date) {
        PortfolioSnapshot portfolioSnapshot = new PortfolioSnapshot(date);

        List<Transaction> buyTransactions = transactionRepository.findByUser(0L, date, Transaction.BUY);
        List<Transaction> sellTransactions = transactionRepository.findByUser(0L, date, Transaction.SELL);

        Map<Stock, List<Transaction>> buyTransactionsOnOrBeforeSamplingDate = buyTransactions.stream().
                filter(
                        transaction -> transaction.getTransactionDate().isBefore(date.plusDays(1))
                ).collect(Collectors.groupingBy(Transaction::getStock));

        Map<Stock, List<Transaction>> sellTransactionsOnOrBeforeSamplingDate = sellTransactions.stream().
                filter(
                        transaction -> transaction.getTransactionDate().isBefore(date.plusDays(1))
                ).collect(Collectors.groupingBy(Transaction::getStock));

        buyTransactionsOnOrBeforeSamplingDate.keySet().forEach(
                stock -> {
                    int buyTransactionsForStock = buyTransactionsOnOrBeforeSamplingDate.
                            getOrDefault(stock, new ArrayList<>()).stream().
                            map(Transaction::getStockCount).
                            reduce(0, Integer::sum);

                    int sellTransactionsForStock = sellTransactionsOnOrBeforeSamplingDate.
                            getOrDefault(stock, new ArrayList<>()).stream().
                            map(Transaction::getStockCount).
                            reduce(0, Integer::sum);

                    int netQuantity = buyTransactionsForStock - sellTransactionsForStock;

                    if(netQuantity > 0) {
                        stock.setPrice(marketService.getQuote(stock, date));
                        stock.setQuantity(netQuantity);
                        portfolioSnapshot.addStock(stock);
                    }
                }
        );

        return portfolioSnapshot;
    }
}
