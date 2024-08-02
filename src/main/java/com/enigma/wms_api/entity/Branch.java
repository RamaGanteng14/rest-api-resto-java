package com.enigma.wms_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "branches")
public class Branch {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String branchId;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "branch")
    private List<Product> products;
}
