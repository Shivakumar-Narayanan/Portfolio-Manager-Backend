package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.model.User;
import com.hackathon.portfoliomanagerapi.repository.TransactionRepository;
import com.hackathon.portfoliomanagerapi.util.TimeStampGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    TimeStampGenerator timeStampGenerator;

    @Autowired
    TransactionRepository transactionRepository;

    public Map<LocalDate, Map<Stock, Integer>> getPortfolioCompositionOverTime(LocalDate startDate, LocalDate endDate) {

        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);

        List<Transaction> buyTransactions = transactionRepository.findByUser(0L, endDate, Transaction.BUY);
        List<Transaction> sellTransactions = transactionRepository.findByUser(0L, endDate, Transaction.SELL);

        Map<LocalDate, Map<Stock, Integer>> res = new HashMap<>();

        for(LocalDate samplingDate : samplingDates) {
            // optimize this later
            Map<Stock, List<Transaction>> buyTransactionsOnOrBeforeSamplingDate = buyTransactions.stream().
                    filter(
                            transaction -> transaction.getTransactionDate().isBefore(samplingDate.plusDays(1))
                    ).collect(Collectors.groupingBy(Transaction::getStock));

            Map<Stock, List<Transaction>> sellTransactionsOnOrBeforeSamplingDate = sellTransactions.stream().
                    filter(
                            transaction -> transaction.getTransactionDate().isBefore(samplingDate.plusDays(1))
                    ).collect(Collectors.groupingBy(Transaction::getStock));

            Map<Stock, Integer> portfolioComposition = new HashMap<>();

            buyTransactionsOnOrBeforeSamplingDate.keySet().forEach(
                    Stock -> {
                        int buyTransactionsForStock = buyTransactionsOnOrBeforeSamplingDate.
                                getOrDefault(Stock, new ArrayList<>()).stream().
                                map(Transaction::getStockCount).
                                reduce(0, Integer::sum);

                        int sellTransactionsForStock = sellTransactionsOnOrBeforeSamplingDate.
                                getOrDefault(Stock, new ArrayList<>()).stream().
                                map(Transaction::getStockCount).
                                reduce(0, Integer::sum);

                        int netQuantity = buyTransactionsForStock - sellTransactionsForStock;

                        if(netQuantity > 0) {
                            portfolioComposition.put(Stock, buyTransactionsForStock - sellTransactionsForStock);
                        }
                    }
            );

            res.put(samplingDate, portfolioComposition);
        }

        return res;
    }
}
