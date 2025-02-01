package com.example.team5_project.service;

import com.example.team5_project.entity.Heart;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Product;
import com.example.team5_project.repository.HeartRepository;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    // todo jwt 관련... ex) jwtUtil

    @Transactional
    public void toggleHeart(
        Long memberId,
        Long productId
    ) {
        /**
         * 상품 좋아요는 등록<->취소 토글식으로 실행됨
         * - Heart 테이블에서 memberId, productId가 일치하는 엔티티가 있으면 좋아요 취소
         * - 없으면 좋아요 등록
         */

        Heart heart = heartRepository.findHeart(memberId, productId);

        // 좋아요 등록
        if(heart == null ) {

            Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("")); //todo 예외처리 핸들러
            Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("")); //todo 예외처리 핸들러

            Heart savedHeart = new Heart(member, product);
            heartRepository.save(savedHeart);
            log.info("상품 아이디: {} 좋아요 등록", productId);
        }
        // 좋아요 취소
        else {
            heartRepository.delete(heart);
            log.info("상품 아이디: {} 좋아요 취소", productId);
        }

    }
}
