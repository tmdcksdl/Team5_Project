package com.example.team5_project.dto.product.response;

public record UserReadProductResponse(Long product_id, String name, int price, int total_likes, int total_view_counts) implements ProductResponse{

}
