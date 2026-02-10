package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.MenuItemRequestDto;
import com.fooddelivery.restaurant.dto.MenuItemResponseDto;
import com.fooddelivery.restaurant.entity.MenuItem;
import com.fooddelivery.restaurant.entity.Restaurant;
import com.fooddelivery.restaurant.exception.ForbiddenException;
import com.fooddelivery.restaurant.exception.ResourceNotFoundException;
import com.fooddelivery.restaurant.mapper.MenuItemMapper;
import com.fooddelivery.restaurant.repository.MenuItemRepository;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import com.fooddelivery.restaurant.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {
    
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;
    
    public MenuItemService(MenuItemRepository menuItemRepository,
                          RestaurantRepository restaurantRepository,
                          MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
    }
    
    @Transactional
    public MenuItemResponseDto addMenuItem(Long restaurantId, MenuItemRequestDto requestDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Only owner can add menu items to their restaurant
        if (!restaurant.getOwnerId().equals(currentUser.getUserId()) && 
            !isAdmin(currentUser)) {
            throw new ForbiddenException("You are not authorized to add menu items to this restaurant");
        }
        
        MenuItem menuItem = menuItemMapper.toEntity(requestDto, restaurantId);
        menuItem.setAvailable(true);
        
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(savedMenuItem);
    }
    
    @Transactional
    public MenuItemResponseDto updateMenuItem(Long menuId, MenuItemRequestDto requestDto) {
        MenuItem menuItem = menuItemRepository.findById(menuId)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuId));
        
        Restaurant restaurant = restaurantRepository.findById(menuItem.getRestaurantId())
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Only owner can update their menu items
        if (!restaurant.getOwnerId().equals(currentUser.getUserId()) && 
            !isAdmin(currentUser)) {
            throw new ForbiddenException("You are not authorized to update this menu item");
        }
        
        menuItemMapper.updateEntityFromDto(requestDto, menuItem);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        
        return menuItemMapper.toDto(updatedMenuItem);
    }
    
    @Transactional
    public void deleteMenuItem(Long menuId) {
        MenuItem menuItem = menuItemRepository.findById(menuId)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuId));
        
        Restaurant restaurant = restaurantRepository.findById(menuItem.getRestaurantId())
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Only owner can delete their menu items
        if (!restaurant.getOwnerId().equals(currentUser.getUserId()) && 
            !isAdmin(currentUser)) {
            throw new ForbiddenException("You are not authorized to delete this menu item");
        }
        
        menuItemRepository.delete(menuItem);
    }
    
    @Transactional
    public MenuItemResponseDto updateMenuItemStatus(Long menuId, Boolean available) {
        MenuItem menuItem = menuItemRepository.findById(menuId)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuId));
        
        Restaurant restaurant = restaurantRepository.findById(menuItem.getRestaurantId())
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Owner or admin can enable/disable menu items
        if (!restaurant.getOwnerId().equals(currentUser.getUserId()) && 
            !isAdmin(currentUser)) {
            throw new ForbiddenException("You are not authorized to change this menu item status");
        }
        
        menuItem.setAvailable(available);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        
        return menuItemMapper.toDto(updatedMenuItem);
    }
    
    public MenuItemResponseDto getMenuItemById(Long menuId) {
        MenuItem menuItem = menuItemRepository.findById(menuId)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuId));
        
        return menuItemMapper.toDto(menuItem);
    }
    
    public List<MenuItemResponseDto> getMenuItemsByRestaurant(Long restaurantId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
            .map(menuItemMapper::toDto)
            .collect(Collectors.toList());
    }
    
    private UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
    
    private boolean isAdmin(UserPrincipal user) {
        return user.getRole().equals("ROLE_ADMIN");
    }
}
