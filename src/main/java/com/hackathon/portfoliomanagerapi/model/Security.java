package com.hackathon.portfoliomanagerapi.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Securities")
@NoArgsConstructor
public class Security {

    public static final String STOCK = "ST";
    public static final String MUTUAL_FUND = "MF";
    public static final String BOND = "BD";
    public static final String ETF = "ET";
    public static final String CASH = "CS";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long securityId;

    private String securityType;

    @Column(unique=true, nullable=false)
    private String ticker;

    private String name;

    private Security(Long securityId) {
        this.securityId = securityId;
    }

    @Override
    public int hashCode() {
        return securityId.hashCode();
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
