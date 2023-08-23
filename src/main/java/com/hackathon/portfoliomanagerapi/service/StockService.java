package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.exceptions.stock.StockExistsException;
import com.hackathon.portfoliomanagerapi.model.Stock;
import com.hackathon.portfoliomanagerapi.model.StockSnapshot;
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

    @Autowired
    MarketService marketService;

    @Value("${maxtypeaheadresults}")
    private int maxTypeAheadResults;

    public void addStock(Stock Stock) throws StockExistsException {

        if(stockRepository.existsByTicker(Stock.getTicker())) {
            throw new StockExistsException();
        }

        stockRepository.save(Stock);
    }

    public List<StockSnapshot> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();

        return augmentStocksWithPrice(stocks);
    }

    public List<StockSnapshot> typeAhead(String query) {
        List<Stock> stocks = stockRepository.findAll();
        stocks.sort(new Stock.StringDistanceComparator(query));

        stocks = stocks.stream().limit(maxTypeAheadResults).collect(Collectors.toList());
        return augmentStocksWithPrice(stocks);
    }

    private List<StockSnapshot> augmentStocksWithPrice(List<Stock> stocks) {
        return stocks.stream().
                map(stock -> new StockSnapshot(stock, marketService.getQuote(stock))).
                collect(Collectors.toList());
    }
}
