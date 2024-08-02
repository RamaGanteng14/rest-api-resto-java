package com.enigma.wms_api.service;

import com.enigma.wms_api.entity.Branch;
import com.enigma.wms_api.model.request.CreateBranchRequest;
import com.enigma.wms_api.model.request.UpdateBranchRequest;
import com.enigma.wms_api.model.response.BranchResponse;
import com.enigma.wms_api.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public BranchResponse create(CreateBranchRequest request){
        validationService.validate(request);

        if (branchRepository.existsByBranchCode(request.getBranchCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch code sudah digunakan!");
        }

        Branch branch = new Branch();
        branch.setBranchId(UUID.randomUUID().toString());
        branch.setBranchCode(request.getBranchCode());
        branch.setBranchName(request.getBranchName());
        branch.setAddress(request.getAddress());
        branch.setPhoneNumber(request.getPhoneNumber());

        branchRepository.save(branch);
        return toBranchResponse(branch);
    }

    public BranchResponse toBranchResponse(Branch branch){
        return BranchResponse.builder()
                .branchId(branch.getBranchId())
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build();
    }

    @Transactional
    public BranchResponse update(UpdateBranchRequest request){
        validationService.validate(request);
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));
        branch.setBranchCode(request.getBranchCode());
        branch.setBranchName(request.getBranchName());
        branch.setAddress(request.getAddress());
        branch.setPhoneNumber(request.getPhoneNumber());
        branchRepository.save(branch);
        return toBranchResponse(branch);
    }

    @Transactional(readOnly = true)
    public BranchResponse get(String branchId){
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));
        return toBranchResponse(branch);
    }

    @Transactional
    public void delete(String branchId){
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan"));
        branchRepository.delete(branch);
    }

    public List<BranchResponse> getAll(){
        List<Branch> branches = branchRepository.findAll();
        List<BranchResponse> branchResponses = branches.stream()
                .map(branch -> BranchResponse.builder()
                        .branchId(branch.getBranchId())
                        .branchCode(branch.getBranchCode())
                        .branchName(branch.getBranchName())
                        .address(branch.getAddress())
                        .phoneNumber(branch.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());
        return branchResponses;
    }

}
