package com.enigma.wms_api.service;

import com.enigma.wms_api.entity.Branch;
import com.enigma.wms_api.entity.Product;
import com.enigma.wms_api.model.request.CreateProductRequest;
import com.enigma.wms_api.model.request.SearchProductRequest;
import com.enigma.wms_api.model.request.UpdateProductRequest;
import com.enigma.wms_api.model.response.BranchResponse;
import com.enigma.wms_api.model.response.ProductResponse;
import com.enigma.wms_api.repository.BranchRepository;
import com.enigma.wms_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ProductService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ProductResponse create(CreateProductRequest request){
        validationService.validate(request);
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch tidak ditemukan"));

        if(productRepository.existsByProductCode(request.getProductCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Code sudah digunakan!");
        }

        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setBranch(branch);
        productRepository.save(product);
        return toProductResponse(product);
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(product.getPrice())
                .branch(toBranchResponse(product.getBranch()))
                .build();
    }

    private BranchResponse toBranchResponse(Branch branch) {
        return BranchResponse.builder()
                .branchId(branch.getBranchId())
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build();
    }


    @Transactional
    public ProductResponse update(UpdateProductRequest request){
        validationService.validate(request);
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch tidak ditemukan"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product tidak ditemukan"));
        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setBranch(branch);
        productRepository.save(product);
        return  toProductResponse(product);
    }

    @Transactional
    public void delete(String productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product tidak ditemukan"));
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProduct(SearchProductRequest request) {
        Specification<Product> productSpecification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getProductCode())) {
                predicates.add(
                        criteriaBuilder.like(root.get("productCode"), "%" + request.getProductCode() + "%"));
            }
            if (Objects.nonNull(request.getProductName())) {
                predicates.add(
                        criteriaBuilder.like(root.get("productName"), "%" + request.getProductName() + "%"));
            }
            if (Objects.nonNull(request.getMinPrice())) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }
            if (Objects.nonNull(request.getMaxPrice())) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Product> products = productRepository.findAll(productSpecification, pageable);

        List<ProductResponse> productResponses = products.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllByBranchId(String branchId){
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch tidak ditemukan"));

        List<Product> products = productRepository.findByBranch(branch);
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

    }
}
