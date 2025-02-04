package com.example.team5_project.controller;

import com.example.team5_project.dto.search.response.FindSearchResponse;
import com.example.team5_project.service.SearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/v1/searches")
    public ResponseEntity<List<FindSearchResponse>> findAllSearchesV1(
    ) {

        return new ResponseEntity<>(searchService.getSearchesV1(), HttpStatus.OK);
    }

    @GetMapping("/v2/searches")
    public ResponseEntity<List<FindSearchResponse>> findAllSearchesV2(
    ) {

        return new ResponseEntity<>(searchService.getSearchesV2(), HttpStatus.OK);
    }
}
