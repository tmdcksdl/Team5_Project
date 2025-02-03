package com.example.team5_project.repository;

import com.example.team5_project.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByName(String name);

    @Query("select s from Store s where s.name = ?1")
    Store findByName(String name);

}

