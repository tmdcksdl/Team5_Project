package com.example.team5_project.repository;

import com.example.team5_project.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query("SELECT s.name, COUNT(s) FROM Search s GROUP BY s.name ORDER BY COUNT(s) DESC")
    List<Object[]> findSearchByName();

    boolean existsByName(String name);
}
