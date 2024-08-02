package com.enigma.wms_api.repository;

import com.enigma.wms_api.entity.Branch;
import com.enigma.wms_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    boolean existsByProductCode(String productCode);
    List<Product> findByBranch(Branch branch);
}
