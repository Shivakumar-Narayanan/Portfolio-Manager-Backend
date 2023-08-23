package com.hackathon.portfoliomanagerapi.repository;

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
}
