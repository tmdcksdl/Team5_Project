package com.example.team5_project.repository;

import com.example.team5_project.dto.orders.response.OrderPageableResponse;
import com.example.team5_project.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query(
            "SELECT new com.example.team5_project.dto.orders.response.OrderPageableResponse(" +
                    "o.id, m.name,p.name,o.orderStatus ) " +
                    "FROM Orders o JOIN o.member m JOIN o.product p "+
                    "WHERE m.id = :memberId"
    )
    Page<OrderPageableResponse> findOrdersByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query(
            "SELECT new com.example.team5_project.dto.orders.response.OrderPageableResponse(" +
                    "o.id, m.name,p.name,o.orderStatus ) " +
                    "FROM Orders o JOIN o.member m JOIN o.product p "+
                    "WHERE p.store.id = :storeId"
    )
    Page<OrderPageableResponse> findOrdersByStoreId(@Param("storeId") Long storeId, Pageable pageable);

}
