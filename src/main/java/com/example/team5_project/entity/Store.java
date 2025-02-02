package com.example.team5_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE store SET is_Deleted = true WHERE store_id = ?")
public class Store extends BaseEntity {

    @Comment("가게 식별자")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT", name = "store_id")
    private Long id;

    @Comment("가게명")
    @Column(unique = true, nullable = false)
    private String name;

    @Comment("멤버 식별자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public static Store create(String name) {
        return new Store(name);
    }

    public void update(String name) {
        this.name = name;
    }

    private Store(String name){
        this.name = name;
    }


}
