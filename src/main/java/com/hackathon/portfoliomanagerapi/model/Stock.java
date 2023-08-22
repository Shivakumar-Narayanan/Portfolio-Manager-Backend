package com.hackathon.portfoliomanagerapi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Stocks")
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stockId;

    @Column(unique=true, nullable=false)
    private String ticker;

    private String name;

    private Stock(Long stockId) {
        this.stockId = stockId;
    }

    @Override
    public int hashCode() {
        return stockId.hashCode();
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
