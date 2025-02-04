package com.example.team5_project.repository;

import com.example.team5_project.entity.Search;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SearchRepository extends JpaRepository<Search, Long> {

    @Query("SELECT s.name, COUNT(s) FROM Search s GROUP BY s.name ORDER BY COUNT(s) DESC")
    List<Object[]> findSearchByName();
}
