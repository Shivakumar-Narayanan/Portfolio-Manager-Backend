package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Security;
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

    public Map<LocalDate, Map<Security, Integer>> getPortfolioCompositionOverTime(LocalDate startDate, LocalDate endDate, List<String> securityTypes) {

        List<LocalDate> samplingDates = timeStampGenerator.generateTimeStamps(startDate, endDate);

        List<Transaction> buyTransactions = transactionRepository.findByUser(0L, endDate, securityTypes, Transaction.BUY);
        List<Transaction> sellTransactions = transactionRepository.findByUser(0L, endDate, securityTypes, Transaction.SELL);

        Map<LocalDate, Map<Security, Integer>> res = new HashMap<>();

        for(LocalDate samplingDate : samplingDates) {
            // optimize this later
            Map<Security, List<Transaction>> buyTransactionsOnOrBeforeSamplingDate = buyTransactions.stream().
                    filter(
                            transaction -> transaction.getTransactionDate().isBefore(samplingDate.plusDays(1))
                    ).collect(Collectors.groupingBy(Transaction::getSecurity));

            Map<Security, List<Transaction>> sellTransactionsOnOrBeforeSamplingDate = sellTransactions.stream().
                    filter(
                            transaction -> transaction.getTransactionDate().isBefore(samplingDate.plusDays(1))
                    ).collect(Collectors.groupingBy(Transaction::getSecurity));

            Map<Security, Integer> portfolioComposition = new HashMap<>();

            buyTransactionsOnOrBeforeSamplingDate.keySet().forEach(
                    security -> {
                        int buyTransactionsForSecurity = buyTransactionsOnOrBeforeSamplingDate.
                                getOrDefault(security, new ArrayList<>()).stream().
                                map(Transaction::getSecurityCount).
                                reduce(0, Integer::sum);

                        int sellTransactionsForSecurity = sellTransactionsOnOrBeforeSamplingDate.
                                getOrDefault(security, new ArrayList<>()).stream().
                                map(Transaction::getSecurityCount).
                                reduce(0, Integer::sum);

                        int netQuantity = buyTransactionsForSecurity - sellTransactionsForSecurity;

                        if(netQuantity > 0) {
                            portfolioComposition.put(security, buyTransactionsForSecurity - sellTransactionsForSecurity);
                        }
                    }
            );

            res.put(samplingDate, portfolioComposition);
        }

        return res;
    }
}
