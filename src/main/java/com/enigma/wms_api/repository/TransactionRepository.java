package com.enigma.wms_api.repository;

import com.enigma.wms_api.entity.Transaction;
import com.enigma.wms_api.entity.TransactionType;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByTransDateBetweenAndTransactionType(LocalDate startDate, LocalDate endDate, TransactionType transactionType);
}

