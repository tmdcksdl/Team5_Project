package com.example.team5_project.repository;

import com.example.team5_project.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByName(String name);
}

