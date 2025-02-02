package com.example.team5_project.service;

import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.dto.store.response.ReadingStoreResponse;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public CreateStoreResponse createStore(CreateStoreRequest requestDto) {

        //TODO 예외처리 어떻게 할지 확인하기
        if (storeRepository.existsByName(requestDto.name())) {
            throw new RuntimeException();
        }

        Store createdStore = Store.create(requestDto.name());

        Store savedStore = storeRepository.save(createdStore);

        return new CreateStoreResponse(savedStore.getId(), savedStore.getName());
    }
    //TODO : 역할에 따른 데이터를 어떻게 줄건지는 미완성
    public List<ReadingStoreResponse> getStore() {

        List<Store> storeList = storeRepository.findAll();

        List<ReadingStoreResponse> responseList = storeList.stream()
                .map(store -> new ReadingStoreResponse(
                        store.getId(), store.getName()))
                .collect(Collectors.toList());

        return responseList;
    }
}
