package com.example.team5_project.repository;

import com.example.team5_project.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    // QueryDSL 사용시 수정하기 todo

    @Query("SELECT h FROM Heart h JOIN FETCH h.product JOIN FETCH h.member "
        + "WHERE h.member.id = :memberId AND h.product.id = :productId")
    Heart findHeart(Long memberId, Long productId);
}
