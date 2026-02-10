package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.dto.MenuItemRequestDto;
import com.fooddelivery.restaurant.dto.MenuItemResponseDto;
import com.fooddelivery.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MenuItemController {
    
    private final MenuItemService menuItemService;
    
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
    
    @PostMapping("/restaurants/{restaurantId}/menu")
    public ResponseEntity<MenuItemResponseDto> addMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequestDto requestDto) {
        MenuItemResponseDto response = menuItemService.addMenuItem(restaurantId, requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/menu/{menuId}")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuItemRequestDto requestDto) {
        MenuItemResponseDto response = menuItemService.updateMenuItem(menuId, requestDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<Map<String, String>> deleteMenuItem(@PathVariable Long menuId) {
        menuItemService.deleteMenuItem(menuId);
        return ResponseEntity.ok(Map.of("message", "Menu item deleted successfully"));
    }
    
    @PatchMapping("/menu/{menuId}/status")
    public ResponseEntity<MenuItemResponseDto> updateMenuItemStatus(
            @PathVariable Long menuId,
            @RequestBody Map<String, Boolean> statusUpdate) {
        Boolean available = statusUpdate.get("available");
        MenuItemResponseDto response = menuItemService.updateMenuItemStatus(menuId, available);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/restaurants/{restaurantId}/menu")
    public ResponseEntity<List<MenuItemResponseDto>> getMenuItemsByRestaurant(
            @PathVariable Long restaurantId) {
        List<MenuItemResponseDto> response = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable Long menuId) {
        MenuItemResponseDto response = menuItemService.getMenuItemById(menuId);
        return ResponseEntity.ok(response);
    }
}
