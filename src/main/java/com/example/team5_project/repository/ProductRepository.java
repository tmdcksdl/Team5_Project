package com.example.team5_project.repository;

import com.example.team5_project.dto.product.response.PageableProductResponse;
import com.example.team5_project.entity.Product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name = ?1")
    Optional<Product> findByName(String name);

    @Modifying
    @Query("UPDATE Product p SET p.totalLikes = p.totalLikes + 1 WHERE p.id = :productId")
    void increaseTotalLikes(Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.totalLikes = p.totalLikes - 1 WHERE p.id = :productId")
    void decreaseTotalLikes(Long productId);

}
