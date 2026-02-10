package com.fooddelivery.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponseDto {
    
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
