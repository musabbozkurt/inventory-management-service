package com.mb.inventorymanagementservice.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@SQLRestriction("deleted=false")
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE products SET deleted=true WHERE id=?")
public class Product extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String productCode;

    private String description;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private int quantity;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
}
