package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.request.UpdateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.dto.store.response.ReadStoreResponse;
import com.example.team5_project.dto.store.response.UpdateStoreResponse;
import com.example.team5_project.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @AuthCheck({"OWNER"})
    @PostMapping
    public ResponseEntity<CreateStoreResponse> createStore(
            @RequestBody CreateStoreRequest requestDto
    ) {
        return new ResponseEntity<>(storeService.createStore(requestDto), HttpStatus.CREATED);
    }

    @AuthCheck({"OWNER", "USER"})
    @GetMapping
    public ResponseEntity<List<ReadStoreResponse>> getStore() {

        return new ResponseEntity<>(storeService.getStore(), HttpStatus.OK);
    }

    @AuthCheck({"OWNER"})
    @PatchMapping("/{storeId}")
    public ResponseEntity<UpdateStoreResponse> updateStore(
            @RequestBody UpdateStoreRequest requestDto,
            @PathVariable Long storeId
    ) {

        return new ResponseEntity<>(storeService.updateStore(storeId, requestDto),HttpStatus.OK);
    }

    @AuthCheck({"OWNER"})
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(
            @PathVariable Long storeId
    ){

        storeService.deleteStore(storeId);

        return new ResponseEntity<>("가게가 삭제되었습니다.", HttpStatus.NO_CONTENT);
    }
}
