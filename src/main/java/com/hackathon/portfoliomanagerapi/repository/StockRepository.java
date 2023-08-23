package com.hackathon.portfoliomanagerapi.repository;

import com.hackathon.portfoliomanagerapi.model.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock, Long> {

    public boolean existsByTicker(String ticker);

    // select * from securities where ticker = given_ticker
    public Stock findByTicker(String ticker);

    public List<Stock> findAll();
}
