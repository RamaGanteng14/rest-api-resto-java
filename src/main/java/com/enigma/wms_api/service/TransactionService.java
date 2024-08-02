package com.enigma.wms_api.service;

import com.enigma.wms_api.entity.*;
import com.enigma.wms_api.model.request.CreateTransactionRequest;
import com.enigma.wms_api.model.request.CreateTransactionDetailRequest;
import com.enigma.wms_api.model.request.SearchTransactionRequest;
import com.enigma.wms_api.model.request.UpdateTransactionRequest;
import com.enigma.wms_api.model.response.*;
import com.enigma.wms_api.repository.BranchRepository;
import com.enigma.wms_api.repository.ProductRepository;
import com.enigma.wms_api.repository.TransactionDetailRepository;
import com.enigma.wms_api.repository.TransactionRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request){
        if(request.getTransactionDetails() == null || request.getTransactionDetails().isEmpty()){
            throw new IllegalArgumentException("Transaction details cannot be null or empty");
        }
        Product firstProduct = productRepository.findById(request.getTransactionDetails().get(0).getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        Branch branch = firstProduct.getBranch();
        if (branch == null){
            throw new IllegalArgumentException("Branch not found");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionType(request.getTransactionType());
        transaction.setBillId(UUID.randomUUID().toString());
        transaction.setTransDate(LocalDate.now());
        transaction.setReceiptNumber(toGenerateReceiptNumber(branch.getBranchCode()));
        Transaction savedTransaction = transactionRepository.save(transaction);

        int total = 0;
        for(CreateTransactionDetailRequest detailRequest : request.getTransactionDetails()){
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            TransactionDetail transactionDetail = new TransactionDetail();
            transactionDetail.setTransaction(savedTransaction);
            transactionDetail.setProduct(product);
            transactionDetail.setQuantity(detailRequest.getQuantity());
            transactionDetailRepository.save(transactionDetail);

            total += product.getPrice() * detailRequest.getQuantity();
        }

        savedTransaction.setTotal(total);
        transactionRepository.save(savedTransaction);

        return toTransactionResponse(savedTransaction);
    }
    @Transactional
    public TransactionResponse updateTransaction(UpdateTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getBillId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan"));

        if (request.getTransactionType() != null) {
            transaction.setTransactionType(request.getTransactionType());
        }

        if (request.getTransactionDetails() != null && !request.getTransactionDetails().isEmpty()) {
            transactionDetailRepository.deleteByTransactionBillId(transaction.getBillId());

            int total = 0;
            for (CreateTransactionDetailRequest detailRequest : request.getTransactionDetails()) {
                Product product = productRepository.findById(detailRequest.getProductId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product tidak ditemukan"));

                TransactionDetail transactionDetail = new TransactionDetail();
                transactionDetail.setProduct(product);
                transactionDetail.setTransaction(transaction);
                transactionDetail.setQuantity(detailRequest.getQuantity());
                transactionDetailRepository.save(transactionDetail);

                total += product.getPrice() * detailRequest.getQuantity();
            }
            transaction.setTotal(total);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        return toTransactionResponse(savedTransaction);
    }
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(String billId){
        Transaction transaction = transactionRepository.findById(billId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan"));
        return toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> search(SearchTransactionRequest request){
        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(Objects.nonNull(request.getReceiptNumber())){
                predicates.add(
                        criteriaBuilder.like(root.get("receiptNumber"), "%" + request.getReceiptNumber() + "%")
                );
            }
            if(Objects.nonNull(request.getProductName())){
                Join<Transaction, Product> productJoin = root.join("transactionDetails").join("product");
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(productJoin.get("productName")),
                        "%" + request.getProductName() + "%")
                );
            }
            if (Objects.nonNull(request.getTransType())){
                predicates.add(
                        criteriaBuilder.equal(root.get("transType"), "%" + request.getTransType() + "%"));
            }
            if (Objects.nonNull(request.getStartDate())){
                LocalDate startDate = LocalDate.parse(request.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("transDate"), startDate)
                );
            }
            if (Objects.nonNull(request.getEndDate())){
                LocalDate endDate = LocalDate.parse(request.getEndDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("transDate"), endDate)
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);

        List<TransactionResponse> transactionResponses = transactions.getContent().stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(transactionResponses, pageable, transactions.getTotalElements());
    }

    @Transactional(readOnly = true)
    public TotalSalesResponse getTotalSales(LocalDate startDate, LocalDate endDate) {

        // Mengambil semua transaksi dengan tipe EATIN
        List<Transaction> eatInTransactions = transactionRepository.findByTransDateBetweenAndTransactionType(startDate, endDate, TransactionType.EAT_IN.name());
        Integer eatInTotal = calculateTotal(eatInTransactions);

        // Mengambil semua transaksi dengan tipe ONLINE
        List<Transaction> onlineTransactions = transactionRepository.findByTransDateBetweenAndTransactionType(startDate, endDate, TransactionType.ONLINE.name());
        Integer onlineTotal = calculateTotal(onlineTransactions);

        // Mengambil semua transaksi dengan tipe TAKEAWAY
        List<Transaction> takeAwayTransactions = transactionRepository.findByTransDateBetweenAndTransactionType(startDate, endDate, TransactionType.TAKE_AWAY.name());
        Integer takeAwayTotal = calculateTotal(takeAwayTransactions);

        // Mengembalikan total penjualan untuk setiap tipe transaksi
        return TotalSalesResponse.builder()
                .eatIn(eatInTotal)
                .online(onlineTotal)
                .takeAway(takeAwayTotal)
                .build();
    }

    private Integer calculateTotal(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getTotal)
                .reduce(0, Integer::sum);
    }



    private String toGenerateReceiptNumber(String branchCode){
        int year = LocalDate.now().getYear();
        long totalTransactional = transactionRepository.count();
        int sequenceNumber = (int) (totalTransactional + 1);
        return String.format("%s-%d-%04d", branchCode, year, sequenceNumber);
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        List<TransactionDetailResponse> detailResponses = transactionDetailRepository.findByTransactionBillId(transaction.getBillId()).stream()
                .map(detail -> {

                    //MAKE TOTAL
                    Product product = detail.getProduct();
                    Integer price = product.getPrice();
                    Integer quantity = detail.getQuantity();
                    Integer totalSales = price * quantity;

                    ProductResponse productResponse = new ProductResponse(
                            detail.getProduct().getProductId(),
                            detail.getProduct().getProductCode(),
                            detail.getProduct().getProductName(),
                            detail.getProduct().getPrice(),
                            new BranchResponse(
                                    detail.getProduct().getBranch().getBranchId(),
                                    detail.getProduct().getBranch().getBranchCode(),
                                    detail.getProduct().getBranch().getBranchName(),
                                    detail.getProduct().getBranch().getAddress(),
                                    detail.getProduct().getBranch().getPhoneNumber()
                            )
                    );

                    return TransactionDetailResponse.builder()
                            .billDetailId(detail.getBillDetailId())
                            .billId(transaction.getBillId())
                            .product(productResponse)
                            .quantity(quantity)
                            .totalSales(BigDecimal.valueOf(totalSales))
                            .build();

                })
                .collect(Collectors.toList());

        return TransactionResponse.builder()
                .billId(transaction.getBillId())
                .receiptNumber(transaction.getReceiptNumber())
                .transDate(transaction.getTransDate())
                .transactionType(transaction.getTransactionType().name())
                .billDetails(detailResponses)
                .build();
    }





}


