package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.exceptions.stock.InvalidStockException;
import com.hackathon.portfoliomanagerapi.exceptions.transaction.*;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.model.User;
import com.hackathon.portfoliomanagerapi.repository.StockRepository;
import com.hackathon.portfoliomanagerapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.hackathon.portfoliomanagerapi.model.Transaction.BUY;
import static com.hackathon.portfoliomanagerapi.model.Transaction.SELL;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    StockRepository stockRepository;

    public void addTransaction(Transaction transaction) throws InvalidStockException,
            InvalidTransactionTypeException,
            InvalidTransactionException,
            InvalidTransactionDateException {

        Stock stock = transaction.getStock();

        validateTransaction(transaction);

        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findByUser(new User(0));
    }

    public void deleteTransaction(Transaction transaction) throws InvalidTransactionDeleteException,
            TransactionDoesntExistException {

        validateDeleteTransaction(transaction);
        transactionRepository.delete(transaction);
    }

    private void validateTransaction(Transaction transaction) throws InvalidStockException,
            InvalidTransactionTypeException,
            InvalidTransactionException,
            InvalidTransactionDateException {

        Stock stock = transaction.getStock();

        if(stock.getStockId() == null && stock.getTicker() == null) {
            throw new InvalidStockException();
        }

        if(stock.getStockId() == null) {
            stock = stockRepository.findByTicker(stock.getTicker());
            transaction.setStock(stock);
        }

        if(stock.getTicker() == null) {
            stock = stockRepository.findByTicker(stock.getTicker());
            transaction.setStock(stock);
        }

        if(transaction.getStock() == null) {
            throw new InvalidStockException();
        }

        if(!(Objects.equals(transaction.getTransactionType(), BUY) || Objects.equals(transaction.getTransactionType(), SELL))) {
            throw new InvalidTransactionTypeException();
        }

        if(transaction.getTransactionType().equals(SELL)) {

            System.out.println(stock);

            Integer totalBuy = transactionRepository.getTransactionsOnOrBeforeDate(0L, transaction.getTransactionDate(), BUY, stock.getTicker());
            Integer totalSell = transactionRepository.getTransactionsOnOrBeforeDate(0L, transaction.getTransactionDate(), SELL, stock.getTicker());

            if(totalBuy == null) {
                totalBuy = 0;
            }
            if(totalSell == null) {
                totalSell = 0;
            }

            int remaining = totalBuy - totalSell;

            if(transaction.getStockCount() > remaining) {
                throw new InvalidTransactionException();
            }
        }

        LocalDate currentDate = LocalDate.now();
        if(transaction.getTransactionDate().isAfter(currentDate)) {
            throw new InvalidTransactionDateException();
        }
    }

    public void validateDeleteTransaction(Transaction transaction) throws TransactionDoesntExistException,
            InvalidTransactionDeleteException {

        Optional<Transaction> transactionOptional = transactionRepository.findById(transaction.getTransactionId());
        if(transactionOptional.isEmpty()) {
            throw new TransactionDoesntExistException();
        }
        transaction = transactionOptional.get();

        if(Objects.equals(transaction.getTransactionType(), SELL)) {
            return;
        }

        Stock stock = transaction.getStock();

        Integer totalBuy = transactionRepository.getTransactionsOnOrBeforeDate(0L, transaction.getTransactionDate(), BUY, stock.getTicker());
        Integer totalSell = transactionRepository.getTransactionsOnOrBeforeDate(0L, transaction.getTransactionDate(), SELL, stock.getTicker());

        if(totalBuy == null) {
            totalBuy = 0;
        }
        if(totalSell == null) {
            totalSell = 0;
        }

        int remainingIfTransactionWasDeleted = totalBuy - totalSell - transaction.getStockCount();

        System.out.println(totalBuy);
        System.out.println(totalSell);
        System.out.println(remainingIfTransactionWasDeleted);

        if(remainingIfTransactionWasDeleted < 0) {
            throw new InvalidTransactionDeleteException();
        }
    }
}
