package com.hackathon.portfoliomanagerapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Transactions")
@NoArgsConstructor
public class Transaction implements Comparable<Transaction> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate transactionDate;

    @ManyToOne()
    @JoinColumn(name = "stockId")
    private Stock stock;

    private int stockCount;

    private double stockPrice;

    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;

    private String transactionType;

    public static final String BUY = "BUY";
    public static final String SELL = "SELL";

    @Override
    public int compareTo(Transaction other) {
        return this.transactionDate.compareTo(other.transactionDate);
    }
}
