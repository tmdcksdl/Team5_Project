package com.example.team5_project.service;

import com.example.team5_project.dto.search.response.FindSearchResponse;
import com.example.team5_project.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    /**
     * product를 조회한 검색어를 조회함
     * @return
     */
    public List<FindSearchResponse> getSearchesV1() {

        List<Object[]> searchList = searchRepository.findSearchByName();

        return searchList
                .stream()
                .map(search -> new FindSearchResponse((String) search[0], (Long) search[1]))
                .limit(10)
                .collect(Collectors.toList());
    }
//
//    /**
//     * product를 조회한 검색어를 조회함 - 캐시 적용됨
//     * @return
//     */
//    @Cacheable(value = "getSearchesV2")
//    public List<FindSearchResponse> getSearchesV2() {
//
//        List<Object[]> searchList = searchRepository.findSearchByName();
//
//        return searchList
//                .stream()
//                .map(search -> new FindSearchResponse((String) search[0], (Long) search[1]))
//                .limit(10)
//                .collect(Collectors.toList());
//    }
}
