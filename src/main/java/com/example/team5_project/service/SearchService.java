package com.example.team5_project.service;

import com.example.team5_project.dto.search.response.FindSearchResponse;
import com.example.team5_project.entity.Search;
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
     * DB에 저장된 검색어들을 count 기준으로 내림차순 정렬하여 조회
     */
    public List<FindSearchResponse> getSearchesV1() {
        List<Search> searchList = searchRepository.findSearchByName();

        return searchList
            .stream()
            .map(search -> new FindSearchResponse(search.getName(), search.getCount()))
            .limit(10)
            .collect(Collectors.toList());
    }
}
