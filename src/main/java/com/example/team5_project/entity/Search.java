package com.example.team5_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "search")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("검색어")
    @Column(nullable = false)
    private String name;

    public static Search of(String name) {
        return new Search(name);
    }

    private Search(String name){
        this.name = name;
    }
}
