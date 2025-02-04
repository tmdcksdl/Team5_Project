package com.example.team5_project.controller;

import com.example.team5_project.dto.search.response.FindSearchResponse;
import com.example.team5_project.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/searches")
    public ResponseEntity<Page<FindSearchResponse>> findAllSearches(
            @RequestParam(name = "size",defaultValue = "5")int size,
            @RequestParam(name = "page", defaultValue = "1")int page
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<FindSearchResponse> searchResponsePage = searchService.getSearches(pageable);

        return new ResponseEntity<>(searchResponsePage, HttpStatus.OK);
    }
}
