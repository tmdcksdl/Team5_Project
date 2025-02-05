package com.example.team5_project.repository;

import com.example.team5_project.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query("SELECT s FROM Search s ORDER BY s.count DESC")
    List<Search> findSearchByName();

    Optional<Search> findByName(String keyword);
}
