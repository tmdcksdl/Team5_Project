package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.heart.response.HeartResponse;
import com.example.team5_project.service.HeartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    /**
     * 상품 좋아요 등록/취소 API
     * - 등록<->취소 토글식으로 실행됨
     */

    @AuthCheck("USER")
    @PostMapping("/products/{productId}/heart")
    public ResponseEntity<Void> toggleHeart(
        HttpServletRequest request,
        @PathVariable("productId") Long productId
    ) {
        String token = request.getHeader("Authorization");
        HttpStatus heartResult = heartService.toggleHeart(token, productId);

        return new ResponseEntity<>(heartResult);
    }

    /**
     * 좋아요 목록 조회
     * - 좋아요 등록일 최신순(내림차순)
     */

    @AuthCheck("USER")
    @GetMapping("/hearts")
    public ResponseEntity<Page<HeartResponse>> getHeartList(
        HttpServletRequest request,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        String token = request.getHeader("Authorization");
        Page<HeartResponse> heartResponsePage = heartService.getHeartList(token, page, size);

        return new ResponseEntity<>(heartResponsePage, HttpStatus.OK);
    }
}
