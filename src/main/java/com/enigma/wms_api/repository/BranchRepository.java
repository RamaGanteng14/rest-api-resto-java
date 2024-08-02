package com.enigma.wms_api.repository;

import com.enigma.wms_api.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {
    boolean existsByBranchCode(String branchCode);
}
