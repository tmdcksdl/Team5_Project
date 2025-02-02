package com.example.team5_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE id = ?")
public class Product extends BaseEntity {

    @Comment("상품 식별자")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Comment("상품명")
    @Column(unique = true, nullable = false)
    private String name;

    @Comment("상품 가격")
    @Column(nullable = false)
    private int price;

    @Comment("재고")
    @Column(nullable = false)
    private int stock;

    @Comment("가게 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

}
