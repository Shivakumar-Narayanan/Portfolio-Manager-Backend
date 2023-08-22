package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.repository.StockRepository;
import com.hackathon.portfoliomanagerapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    StockRepository stockRepository;

    public void addTransaction(Transaction transaction) {
        Stock stock = transaction.getStock();
        if(stock.getStockId() == null) {
            transaction.setStock(stockRepository.findByTicker(stock.getTicker()));
        }

        transactionRepository.save(transaction);
    }
}
