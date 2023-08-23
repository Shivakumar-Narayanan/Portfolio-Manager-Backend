package com.hackathon.portfoliomanagerapi.repository;

import com.hackathon.portfoliomanagerapi.model.StockSnapshot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockSnapshotRepository extends CrudRepository<StockSnapshot, Long> {

    @Query(
            value = "select BT.stock_id as stock_snapshot_id, BT.stock_id as stock_id, (BT.amount - IFNULL(ST.amount, 0)) as total_buy_amount , (BT.stock_count - IFNULL(ST.stock_count, 0)) as quantity, ((BT.amount - IFNULL(ST.amount, 0)) / (BT.stock_count - IFNULL(ST.stock_count, 0))) as buy_price_per_share from " +
                    "(select stock_id, sum(stock_price * stock_count) as amount, sum(stock_count) as stock_count " +
                    "from transactions " +
                    "where transaction_type='BUY' and transaction_date <= ?2 and user_id = ?1 " +
                    "group by stock_id " +
                    ") BT " +
                    "left join " +
                    "(select stock_id, sum(stock_price * stock_count) as amount, sum(stock_count) as stock_count " +
                    "from transactions " +
                    "where transaction_type='SELL' and transaction_date <= ?2 and user_id = ?1 " +
                    "group by stock_id " +
                    ") ST " +
                    "on BT.stock_id = ST.stock_id " +
                    "where (BT.stock_count - IFNULL(ST.stock_count, 0)) > 0;", nativeQuery=true
    )
    public List<StockSnapshot> getPortfolioCompositionAsOn(Long userId, LocalDate date);
}
