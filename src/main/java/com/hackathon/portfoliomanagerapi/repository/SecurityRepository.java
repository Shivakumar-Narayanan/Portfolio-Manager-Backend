package com.hackathon.portfoliomanagerapi.repository;

import com.hackathon.portfoliomanagerapi.model.Security;
import org.springframework.data.repository.CrudRepository;

public interface SecurityRepository extends CrudRepository<Security, Long> {

    public boolean existsByTicker(String ticker);

    public Security findByTicker(String ticker);
}
