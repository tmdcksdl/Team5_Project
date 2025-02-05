package com.example.team5_project.service;

import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.exception.ProductException;
import com.example.team5_project.common.exception.errorcode.MemberErrorCode;
import com.example.team5_project.common.exception.errorcode.ProductErrorCode;
import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.dto.heart.response.HeartResponse;
import com.example.team5_project.dto.product.response.ReadProductResponse;
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
    private final JwtUtil jwtUtil;

    @Transactional
    public HttpStatus toggleHeart(
        String token,
        Long productId
    ) {
        /**
         * 상품 좋아요는 등록<->취소 토글식으로 실행됨
         * - Heart 테이블에서 memberId, productId가 일치하는 엔티티가 있으면 좋아요 취소
         * - 없으면 좋아요 등록
         */
        Long memberId = Long.valueOf(jwtUtil.extractId(token)); // memberId 정보 추출
        Heart heart = heartRepository.findHeart(memberId, productId);

        // 좋아요 등록
        if(heart == null ) {

            Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
            Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));

            Heart savedHeart = new Heart(member, product);
            heartRepository.save(savedHeart);

            // product 엔티티 totalLikes +1
            productRepository.increaseTotalLikes(productId);
            log.info("좋아요 등록 완료 >>> 상품 아이디: {}", productId);

            return HttpStatus.CREATED;
        }
        // 좋아요 취소
        else {
            heartRepository.delete(heart);

            // product 엔티티 totalLikes -1
            productRepository.decreaseTotalLikes(productId);
            log.info("좋아요 취소 완료 >>> 상품 아이디: {}", productId);

            return HttpStatus.NO_CONTENT;
        }
    }

    @Transactional
    public Page<HeartResponse> getHeartList(String token, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Long memberId = Long.valueOf(jwtUtil.extractId(token)); // memberId 정보 추출

        Page<Heart> hearts = heartRepository.findHearts(memberId, pageable);

        return hearts.map(heart -> new HeartResponse(
            heart.getId(), memberId,
            new ReadProductResponse(
                heart.getProduct().getId(), heart.getProduct().getName(),
                heart.getProduct().getPrice(), heart.getProduct().getStock(),
                heart.getProduct().getTotalLikes()),
            heart.getCreatedAt()
        ));
    }
}
