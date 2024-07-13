package com.mb.inventorymanagementservice.data.entity;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.javamoney.moneta.Money;

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
    @CompositeType(MonetaryAmountType.class)
    @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    @AttributeOverride(name = "amount", column = @Column(name = "current_price"))
    private Money price;

    @Column(nullable = false)
    private int quantity;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
}
