package com.fooddelivery.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private Long ownerId;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
