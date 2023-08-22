package com.hackathon.portfoliomanagerapi.controller;

import com.hackathon.portfoliomanagerapi.exceptions.SecurityExistsException;
import com.hackathon.portfoliomanagerapi.model.Security;
import com.hackathon.portfoliomanagerapi.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/securities")
public class SecurityController {

    @Autowired
    SecurityService securityService;

    @PostMapping("/addSecurity")
    public ResponseEntity<String> addSecurity(@RequestBody Security security) {
        try {
            System.out.println("Request to create security: " + security);
            securityService.addSecurity(security);
            return ResponseEntity.ok("Security Added");
        }
        catch(SecurityExistsException securityExistsException) {
            return ResponseEntity.ok("Security Already Exists");
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Could not add security");
        }
    }
}
