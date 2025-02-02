package com.example.team5_project.controller;

import com.example.team5_project.dto.heart.response.HeartResponse;
import com.example.team5_project.service.HeartService;
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

    // jwt 완성 전 api 작동 실험용
    @PostMapping("/products/{productId}/heart")
    public ResponseEntity<Void> toggleHeart(
        @RequestParam Long memberId,
        @PathVariable("productId") Long productId
    ) {

        HttpStatus heartResult = heartService.toggleHeart(memberId, productId);

        return new ResponseEntity<>(heartResult);
    }

    // todo 인증인가 완성 후 실제 api (대략적으로)
//    @AuthCheck("USER")
//    @PostMapping("/products/{productId}/heart")
//    public ResponseEntity<Void> toggleHeart(
//        HttpServlet request,
//        @PathVariable("productId") Long productId
//    ) {
//        String token = request.getHeader("Autorizaion");
//        heartService.toggleHeart(token, productId);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    /**
     * 좋아요 목록 조회
     * - 좋아요 등록일 최신순(내림차순)
     */

    // API 동작 실험용
    @GetMapping("/hearts")
    public ResponseEntity<Page<HeartResponse>> getHeartList(
        @RequestParam Long memberId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(heartService.getHeartList(memberId, page, size), HttpStatus.OK);
    }

    // todo 인증인가 완성 후 실제 api (대략적으로)
//    @GetMapping("/hearts")
//    public ResponseEntity<Page<HeartResponse>> getHeartList(
//        HttpServlet request,
//        @RequestParam(defaultValue = "1") int page,
//        @RequestParam(defaultValue = "10") int size
//    ) {
//        String token = request.getHeader("Autorizaion");
//        heartService.toggleHeart(token, productId);

//        return new ResponseEntity<>(heartService.getHeartList(memberId, page, size), HttpStatus.OK);
//    }



}
