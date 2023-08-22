package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.model.Security;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.repository.SecurityRepository;
import com.hackathon.portfoliomanagerapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    SecurityRepository securityRepository;

    public void addTransaction(Transaction transaction) {
        Security security = transaction.getSecurity();
        if(security.getSecurityId() == null) {
            transaction.setSecurity(securityRepository.findByTicker(security.getTicker()));
        }

        transactionRepository.save(transaction);
    }
}
