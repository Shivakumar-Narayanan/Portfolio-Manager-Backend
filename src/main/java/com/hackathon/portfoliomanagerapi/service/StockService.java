package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.exceptions.StockExistsException;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    StockRepository stockRepository;

    @Value("${maxtypeaheadresults}")
    private int maxTypeAheadResults;

    public void addStock(Stock Stock) throws StockExistsException {

        if(stockRepository.existsByTicker(Stock.getTicker())) {
            throw new StockExistsException();
        }

        stockRepository.save(Stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public List<Stock> typeAhead(String query) {
        List<Stock> stocks = stockRepository.findAll();
        stocks.sort(new Stock.StringDistanceComparator(query));

        return stocks.stream().limit(maxTypeAheadResults).collect(Collectors.toList());
    }
}
