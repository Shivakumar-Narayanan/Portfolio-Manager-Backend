package com.hackathon.portfoliomanagerapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StockSnapshot implements Serializable, Comparable<StockSnapshot> {

    @Id
    Long stockSnapshotId;

    @ManyToOne()
    @JoinColumn(name = "stockId")
    Stock stock;

    private double totalBuyAmount;

    private double buyPricePerShare;

    @Transient
    private double currentPricePerShare;

    @Transient
    private double currentTotalAmount;

    private int quantity;

    public StockSnapshot(Stock stock, double buyAmount, int quantity) {
        this.stock = stock;
        this.totalBuyAmount = buyAmount;
        this.quantity = quantity;
        this.buyPricePerShare = buyAmount / quantity;
    }

    public StockSnapshot(Stock stock, double currentPricePerShare) {
        this.stock = stock;
        this.currentPricePerShare = currentPricePerShare;
    }

    public void setCurrentPricePerShare(double currentPricePerShare) {
        this.currentPricePerShare = currentPricePerShare;
        this.currentTotalAmount = currentPricePerShare * quantity;
    }

    @Override
    public int compareTo(StockSnapshot other) {
        double this_profit = currentTotalAmount - totalBuyAmount;
        double other_profit = other.currentTotalAmount - other.totalBuyAmount;

        double this_profit_percentage = this_profit / totalBuyAmount;
        double other_profit_percentage = other_profit / other.totalBuyAmount;

        if(this_profit_percentage >= other_profit_percentage) {
            return -1;
        }
        return 1;
    }
}
