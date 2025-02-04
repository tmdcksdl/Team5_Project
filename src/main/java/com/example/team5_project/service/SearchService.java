package com.example.team5_project.service;

import com.example.team5_project.dto.search.response.FindSearchResponse;
import com.example.team5_project.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    /**
     * product를 조회한 검색어를 조회함
     * @param pageable
     * @return
     */
    public Page<FindSearchResponse> getSearches(Pageable pageable) {

        Page<Object[]> searchByName = searchRepository.findSearchByName(pageable);

        return searchByName.map(search -> new FindSearchResponse((String) search[0], (Long) search[1]));
    }
}
