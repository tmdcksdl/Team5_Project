package com.example.team5_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final String SEARCH_KEY_PREFIX = "search:";  // 검색어 Key
    private static final String POPULAR_SEARCH_KEY = "popular_searches";  // 인기 검색어 저장 Key

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 검색어 저장 및 검색 횟수 증가
     * - 검색어도 저장하고 인기 검색어의 count도 증가
     */
    public void saveKeywordToCache(String keyword) {
        String key = SEARCH_KEY_PREFIX + keyword;

        // 검색어 자체를 저장
        redisTemplate.opsForValue().set(key, keyword, 10, TimeUnit.MINUTES);

        // 인기 검색어 카운트 증가
        redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, keyword, 1);
    }

    /**
     * 저장된 인기 검색어 조회
     */
    public Set<String> getPopularKeywords(int limit) {

        return redisTemplate.opsForZSet()
                .reverseRange(POPULAR_SEARCH_KEY, 0, limit - 1);  // 점수가 높은 순으로 조회
    }

    /**
     * 저장된 특정 검색어 삭제
     */
    public void deleteKeyword(String keyword) {
        redisTemplate.delete(SEARCH_KEY_PREFIX + keyword);
        redisTemplate.opsForZSet().remove(POPULAR_SEARCH_KEY, keyword);
    }

    /**
     * 저장된 전체 인기 검색어 삭제
     */
    public void clearPopularKeywords() {
        redisTemplate.delete(POPULAR_SEARCH_KEY);
    }
}
