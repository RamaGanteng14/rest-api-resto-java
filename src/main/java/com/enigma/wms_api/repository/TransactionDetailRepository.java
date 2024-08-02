package com.enigma.wms_api.repository;

import com.enigma.wms_api.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
    List<TransactionDetail> findByTransactionBillId(String billId);
    void deleteByTransactionBillId(String billId);
}
