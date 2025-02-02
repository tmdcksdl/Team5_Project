package com.example.team5_project.controller;

import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<CreateStoreResponse> createStore(
            @RequestBody CreateStoreRequest requestDto
    ) {

        CreateStoreResponse store = storeService.createStore(requestDto);

        return new ResponseEntity<>(store, HttpStatus.CREATED);
    }
}
