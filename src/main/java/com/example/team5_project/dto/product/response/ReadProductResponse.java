package com.example.team5_project.dto.product.response;

public record ReadProductResponse(Long product_id, String name, int price, int stock, int total_likes) {

}
