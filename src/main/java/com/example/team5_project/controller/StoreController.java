package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.request.UpdateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.dto.store.response.ReadStoreResponse;
import com.example.team5_project.dto.store.response.UpdateStoreResponse;
import com.example.team5_project.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 생성
     */
    @AuthCheck({"OWNER"})
    @PostMapping
    public ResponseEntity<CreateStoreResponse> createStore(
        @RequestBody CreateStoreRequest requestDto,
        HttpServletRequest httpServletRequest
    ) {
        CreateStoreResponse storeResponse = storeService.createStore(requestDto, httpServletRequest);

        return new ResponseEntity<>(storeResponse, HttpStatus.CREATED);
    }

    /**
     * 가게 전체 조회
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping
    public ResponseEntity<Page<ReadStoreResponse>> getStore(
        @RequestParam(name = "size",defaultValue = "5")int size,
        @RequestParam(name = "page", defaultValue = "1")int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReadStoreResponse> storeResponsePage = storeService.getStore(pageable);

        return new ResponseEntity<>(storeResponsePage, HttpStatus.OK);
    }

    /**
     * 가게 정보 수정
     */
    @AuthCheck({"OWNER"})
    @PatchMapping("/{storeId}")
    public ResponseEntity<UpdateStoreResponse> updateStore(
        @RequestBody UpdateStoreRequest requestDto,
        @PathVariable Long storeId
    ) {
        UpdateStoreResponse storeResponse = storeService.updateStore(storeId, requestDto);

        return new ResponseEntity<>(storeResponse, HttpStatus.OK);
    }

    /**
     * 가게 삭제
     */
    @AuthCheck({"OWNER"})
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(
        @PathVariable Long storeId
    ){
        storeService.deleteStore(storeId);

        return new ResponseEntity<>("가게가 삭제되었습니다.", HttpStatus.NO_CONTENT);
    }
}
