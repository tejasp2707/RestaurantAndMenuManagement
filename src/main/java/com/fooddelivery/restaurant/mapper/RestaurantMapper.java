package com.fooddelivery.restaurant.mapper;

import com.fooddelivery.restaurant.dto.RestaurantRequestDto;
import com.fooddelivery.restaurant.dto.RestaurantResponseDto;
import com.fooddelivery.restaurant.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    
    public Restaurant toEntity(RestaurantRequestDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setAddress(dto.getAddress());
        restaurant.setPhone(dto.getPhone());
        return restaurant;
    }
    
    public RestaurantResponseDto toDto(Restaurant restaurant) {
        return new RestaurantResponseDto(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getDescription(),
            restaurant.getAddress(),
            restaurant.getPhone(),
            restaurant.getOwnerId(),
            restaurant.getActive(),
            restaurant.getCreatedAt(),
            restaurant.getUpdatedAt()
        );
    }
    
    public void updateEntityFromDto(RestaurantRequestDto dto, Restaurant restaurant) {
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setAddress(dto.getAddress());
        restaurant.setPhone(dto.getPhone());
    }
}
