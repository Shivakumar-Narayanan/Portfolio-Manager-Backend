package com.hackathon.portfoliomanagerapi.repository;

import com.hackathon.portfoliomanagerapi.model.StockSnapshot;
import com.hackathon.portfoliomanagerapi.model.Transaction;
import com.hackathon.portfoliomanagerapi.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    public List<Transaction> findByUser(User user);

    @Query(
            value = "SELECT * FROM Transactions T " +
                    "NATURAL JOIN Stocks S " +
                    "WHERE T.user_id=?1 " +
                    "AND T.transaction_date <= ?2 " +
                    "AND T.transaction_type=?3 " +
                    "ORDER BY T.transaction_date", nativeQuery=true)
    public List<Transaction> findByUser(Long userId, LocalDate tillDate, String transactionType);

    @Query(
            value = "SELECT SUM(stock_count) FROM Transactions T " +
                    "NATURAL JOIN Stocks S " +
                    "WHERE T.user_id=?1 " +
                    "AND T.transaction_date <= ?2 " +
                    "AND T.transaction_type=?3 " +
                    "AND S.ticker=?4", nativeQuery=true
    )
    public Integer getTransactionsOnOrBeforeDate(Long userId, LocalDate tillDate, String transactionType, String ticker);

    @Query(
            value = "select sum(BT.amount - IFNULL(ST.amount, 0)) as amount from " +
                    "(select stock_id, sum(stock_price * stock_count) as amount, sum(stock_count) as stock_count " +
                    "from transactions " +
                    "where transaction_type='BUY' and transaction_date <= ?2 and user_id = ?1 " +
                    "group by stock_id " +
                    ") BT " +
                    "left join " +
                    "(select stock_id, sum(stock_price * stock_count) as amount, sum(stock_count) as stock_count  " +
                    "from transactions  " +
                    "where transaction_type='SELL' and transaction_date <= ?2 and user_id = ?1" +
                    "group by stock_id" +
                    ") ST " +
                    "on BT.stock_id = ST.stock_id", nativeQuery=true)
    public Double getTotalInvestedAsOn(Long userId, LocalDate date);
}
