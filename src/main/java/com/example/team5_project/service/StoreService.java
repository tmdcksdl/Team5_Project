package com.example.team5_project.service;

import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.request.UpdateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.dto.store.response.ReadStoreResponse;
import com.example.team5_project.dto.store.response.UpdateStoreResponse;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public CreateStoreResponse createStore(CreateStoreRequest requestDto) {

        //TODO 예외처리 어떻게 할지 확인하기
        if (storeRepository.existsByName(requestDto.name())) {
            throw new RuntimeException();
        }

        Store createdStore = Store.create(requestDto.name());

        Store savedStore = storeRepository.save(createdStore);

        return new CreateStoreResponse(savedStore.getId(), savedStore.getName());
    }

    public List<ReadStoreResponse> getStore() {

        List<Store> storeList = storeRepository.findAll();

        List<ReadStoreResponse> responseList = storeList.stream()
                .map(store -> new ReadStoreResponse(
                        store.getId(), store.getName()))
                .collect(Collectors.toList());

        return responseList;
    }

    @Transactional
    public UpdateStoreResponse updateStore(Long storeId, UpdateStoreRequest requestDto) {

        Optional<Store> byName = Optional.ofNullable(storeRepository.findByName(requestDto.name()));

        if (byName.isPresent()) {
            throw new RuntimeException();
        }

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException());

        foundStore.update(requestDto.name());

        return new UpdateStoreResponse(storeId, foundStore.getName());
    }

    @Transactional
    public void deleteStore(Long storeId) {

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException());

        storeRepository.delete(foundStore);
        log.info("Found store: {}, isDeleted: {}", foundStore, foundStore.isDeleted());

    }
}
