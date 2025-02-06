package com.example.team5_project.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CacheService {

    /**
     * ConcurrentHashMap을 사용해서 인기 검색어와 검색 횟수를 저장할 Cache 생성
     */
    private final Map<String, Integer> searchCountCache = new ConcurrentHashMap<>();

    /**
     * 검색어를 캐시에 저장
     * - 검색 횟수도 함께 증가
     */
    public void saveKeywordToCache(String keyword) {
        searchCountCache.merge(keyword, 1, Integer::sum);  // 기존 값에 1 증가
    }

    /**
     * 캐시에 저장된 인기 검색어 조회
     */
    public List<String> getPopularKeywords(int limit) {

        return searchCountCache.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))  // 검색 횟수 내림차순 정렬
            .limit(limit)  // 상위 검색어 반환
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    /**
     * 캐시에 저장된 특정 검색어 삭제
     */
    public void removeKeyword(String keyword) {
        searchCountCache.remove(keyword);
    }

    /**
     * 캐시에 저장된 전체 인기 검색어 삭제
     */
    public void clearCache() {
        searchCountCache.clear();
    }
}
