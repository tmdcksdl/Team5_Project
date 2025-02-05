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

    @GetMapping("/v1/searches")
    public ResponseEntity<List<FindSearchResponse>> findAllSearchesV1(
    ) {

        return new ResponseEntity<>(searchService.getSearchesV1(), HttpStatus.OK);
    }

    // 인기 검색어 조회 API (상위 10개 검색어 조회)
    @GetMapping("/searches/popular")
    public ResponseEntity<Set<String>> getPopularKeywords(
            @RequestParam(defaultValue = "10") int limit
    ) {

//        List<String> popularKeywords = cacheService.getPopularKeywords(limit);
        Set<String> popularKeywords = redisService.getPopularKeywords(limit);

        return new ResponseEntity<>(popularKeywords, HttpStatus.OK);
    }

    // 특정 검색어 캐시 삭제 API
    @DeleteMapping("/searches/cache")
    public ResponseEntity<Void> evictCache(
            @RequestParam String keyword
    ) {

//        cacheService.removeKeyword(keyword);
        redisService.deleteKeyword(keyword);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 전체 검색어 캐시 삭제 API
    @DeleteMapping("/searches/cache/all")
    public ResponseEntity<Void> clearAllCache() {

//        cacheService.clearCache();
        redisService.clearPopularKeywords();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//    @GetMapping("/v2/searches")
//    public ResponseEntity<List<FindSearchResponse>> findAllSearchesV2(
//    ) {
//
//        return new ResponseEntity<>(searchService.getSearchesV2(), HttpStatus.OK);
//    }
}
