package com.example.team5_project.controller;

import com.example.team5_project.dto.search.response.FindSearchResponse;
import com.example.team5_project.service.CacheService;
import com.example.team5_project.service.RedisService;
import com.example.team5_project.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final CacheService cacheService;
    private final RedisService redisService;

    /**
     * DB에서 인기 검색어 조회
     * - 조회수를 기준으로 내림차순 정렬되어 조회
     */
    @GetMapping("/v1/searches")
    public ResponseEntity<List<FindSearchResponse>> findAllSearchesV1() {
        List<FindSearchResponse> searchResponseList = searchService.getSearchesV1();

        return new ResponseEntity<>(searchResponseList, HttpStatus.OK);
    }

    /**
     * 인기 검색어 조회 API (상위 10개 검색어 조회)
     * - Redis에 저장된 인기 검색어 조회
     */
    @GetMapping("/searches/popular")
    public ResponseEntity<Set<String>> getPopularKeywords(
        @RequestParam(defaultValue = "10") int limit
    ) {
        //List<String> popularKeywords = cacheService.getPopularKeywords(limit);  // 캐시 적용 시 사용
        Set<String> popularKeywords = redisService.getPopularKeywords(limit);  // Redis 적용 시 사용

        return new ResponseEntity<>(popularKeywords, HttpStatus.OK);
    }

    /**
     * 캐시에서 특정 검색어 삭제
     */
    @DeleteMapping("/searches/cache")
    public ResponseEntity<Void> evictCache(
        @RequestParam String keyword
    ) {
        // cacheService.removeKeyword(keyword);  // 캐시 적용 시 사용
        redisService.deleteKeyword(keyword);  // Redis 적용 시 사용

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 캐시에 저장된 전체 검색어 삭제
     */
    @DeleteMapping("/searches/cache/all")
    public ResponseEntity<Void> clearAllCache() {
        // cacheService.clearCache();  // 캐시 적용 시 사용
        redisService.clearPopularKeywords();  // Redis 적용 시 사용

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
