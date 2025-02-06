package com.example.team5_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.team5_project.dto.store.request.CreateStoreRequest;
import com.example.team5_project.dto.store.request.UpdateStoreRequest;
import com.example.team5_project.dto.store.response.CreateStoreResponse;
import com.example.team5_project.dto.store.response.ReadStoreResponse;
import com.example.team5_project.dto.store.response.UpdateStoreResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    StoreRepository storeRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    StoreService storeService;


    Member member = mock(Member.class);
    Store store = new Store(1L, "name", member);

    @Test
    public void 가게_생성_성공() throws Exception {

        Member member = mock(Member.class);
        Store store = new Store(1L, "testStore", member);

        CreateStoreRequest requestDto = new CreateStoreRequest("testStore");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("id", 1L);

        // given
        when(storeRepository.existsByName(requestDto.name())).thenReturn(false);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        // when
        CreateStoreResponse response = storeService.createStore(requestDto, request);
        // then
        assertEquals("testStore", response.name());
        assertEquals(1L,response.store_id());

    }

    @Test
    public void 가게_조회_성공() throws Exception {

        // given

        Pageable pageable = PageRequest.of(0, 10);

        List<Store> storeList = List.of(
                Store.create("Store1", new Member()),
                Store.create("Store2", new Member())
        );

        Page<Store> stores = new PageImpl<>(storeList, pageable, storeList.size());
        when(storeRepository.findStores(pageable)).thenReturn(stores);
        // when
        Page<ReadStoreResponse> result = storeService.getStore(pageable);
        // then
        assertThat(result).hasSize(2);
        assertThat(result.getContent().get(0)).isInstanceOf(ReadStoreResponse.class);
        assertThat(result.getContent().get(0).name()).isEqualTo("Store1");
    }

    @Test
    public void 가게_수정_성공() throws Exception {
        // given
        UpdateStoreRequest request = new UpdateStoreRequest("UpdatedProduct");
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        // when
        UpdateStoreResponse response = storeService.updateStore(store.getId(), request);
        // then
        assertThat(response.name()).isEqualTo("UpdatedProduct");

    }

    @Test
    public void 가게_삭제_성공() throws Exception {
        // given
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        doNothing().when(storeRepository).delete(store);
        // when
        storeService.deleteStore(store.getId());
        // then
        verify(storeRepository, times(1)).delete(store);
    }

}