package com.hackathon.portfoliomanagerapi.service;

import com.hackathon.portfoliomanagerapi.exceptions.SecurityExistsException;
import com.hackathon.portfoliomanagerapi.model.Security;
import com.hackathon.portfoliomanagerapi.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    SecurityRepository securityRepository;

    public void addSecurity(Security security) throws SecurityExistsException {

        if(securityRepository.existsByTicker(security.getTicker())) {
            throw new SecurityExistsException();
        }

        securityRepository.save(security);
    }
}
