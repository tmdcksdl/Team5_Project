package com.example.team5_project.service;

import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public CreateStoreResponse createStore(CreateStoreRequest requestDto){

        Store createdStore = Store.create(requestDto.name());

        Store savedStore = storeRepository.save(createdStore);

        return new CreateStoreResponse(savedStore.getId(), savedStore.getName());
    }

}
