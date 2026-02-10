package com.fooddelivery.restaurant.mapper;

import com.fooddelivery.restaurant.dto.MenuItemRequestDto;
import com.fooddelivery.restaurant.dto.MenuItemResponseDto;
import com.fooddelivery.restaurant.entity.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    
    public MenuItem toEntity(MenuItemRequestDto dto, Long restaurantId) {
        MenuItem menuItem = new MenuItem();
        menuItem.setRestaurantId(restaurantId);
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCategory(dto.getCategory());
        return menuItem;
    }
    
    public MenuItemResponseDto toDto(MenuItem menuItem) {
        return new MenuItemResponseDto(
            menuItem.getId(),
            menuItem.getRestaurantId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getAvailable(),
            menuItem.getCategory(),
            menuItem.getCreatedAt(),
            menuItem.getUpdatedAt()
        );
    }
    
    public void updateEntityFromDto(MenuItemRequestDto dto, MenuItem menuItem) {
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCategory(dto.getCategory());
    }
}
