package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.exceptions.StockExistsException;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    StockRepository StockRepository;

    public void addStock(Stock Stock) throws StockExistsException {

        if(StockRepository.existsByTicker(Stock.getTicker())) {
            throw new StockExistsException();
        }

        StockRepository.save(Stock);
    }
}
