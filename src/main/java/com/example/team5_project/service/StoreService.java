package com.example.team5_project.service;

import com.example.team5_project.common.exception.StoreException;
import com.example.team5_project.common.exception.errorcode.StoreErrorCode;
import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.request.UpdateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.dto.store.response.ReadStoreResponse;
import com.example.team5_project.dto.store.response.UpdateStoreResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.StoreRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateStoreResponse createStore(CreateStoreRequest requestDto, HttpServletRequest request) {

        Long memberId = (Long) request.getAttribute("id");

        //TODO :: MEMBER 완성되면 받아 쓰기
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException());


        if (storeRepository.existsByName(requestDto.name())) {
            throw new StoreException(StoreErrorCode.ALREADY_EXIST_STORE);
        }

        Store createdStore = Store.create(requestDto.name(), foundMember);

        Store savedStore = storeRepository.save(createdStore);

        return new CreateStoreResponse(savedStore.getId(), savedStore.getName());
    }

    public Page<ReadStoreResponse> getStore(Pageable pageable) {

        Page<Store> storePage = storeRepository.findStores(pageable);

        return storePage.map(store -> new ReadStoreResponse(
                store.getId(), store.getName()
        ));
    }

    @Transactional
    public UpdateStoreResponse updateStore(Long storeId, UpdateStoreRequest requestDto) {

        if (storeRepository.existsByName(requestDto.name())) {
            throw new StoreException(StoreErrorCode.ALREADY_EXIST_STORE);
        }

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND_STORE));

        foundStore.update(requestDto.name());

        return new UpdateStoreResponse(storeId, foundStore.getName());
    }

    @Transactional
    public void deleteStore(Long storeId) {

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND_STORE));

        storeRepository.delete(foundStore);

    }
}
