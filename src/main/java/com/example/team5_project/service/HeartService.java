package com.example.team5_project.service;

import com.example.team5_project.dto.heart.response.HeartResponse;
import com.example.team5_project.dto.product.response.ProductResponse;
import com.example.team5_project.entity.Heart;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Product;
import com.example.team5_project.repository.HeartRepository;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public HttpStatus toggleHeart(
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

            // todo productRepository.좋아요 수 1증가
            return HttpStatus.CREATED;
        }
        // 좋아요 취소
        else {
            heartRepository.delete(heart);
            log.info("상품 아이디: {} 좋아요 취소", productId);

            return HttpStatus.NO_CONTENT;
        }
    }

    @Transactional
    public Page<HeartResponse> getHeartList(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Heart> hearts = heartRepository.findHearts(memberId, pageable);

        return hearts.map(heart -> new HeartResponse(
            heart.getId(),
            memberId,
            heart.getProduct().getId(),
            new ProductResponse(
                heart.getProduct().getId(), heart.getProduct().getName(),
                heart.getProduct().getPrice(), heart.getProduct().getTotalLikes()),
            heart.getCreatedAt()
        ));
    }
}
