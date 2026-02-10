package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.RestaurantRequestDto;
import com.fooddelivery.restaurant.dto.RestaurantResponseDto;
import com.fooddelivery.restaurant.entity.Restaurant;
import com.fooddelivery.restaurant.exception.ForbiddenException;
import com.fooddelivery.restaurant.exception.ResourceNotFoundException;
import com.fooddelivery.restaurant.mapper.RestaurantMapper;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import com.fooddelivery.restaurant.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    
    public RestaurantService(RestaurantRepository restaurantRepository, 
                           RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }
    
    @Transactional
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto requestDto) {
        UserPrincipal currentUser = getCurrentUser();
        
        Restaurant restaurant = restaurantMapper.toEntity(requestDto);
        restaurant.setOwnerId(currentUser.getUserId());
        restaurant.setActive(true);
        
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(savedRestaurant);
    }
    
    @Transactional
    public RestaurantResponseDto updateRestaurant(Long id, RestaurantRequestDto requestDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Only owner can update their restaurant
        if (!restaurant.getOwnerId().equals(currentUser.getUserId()) && 
            !isAdmin(currentUser)) {
            throw new ForbiddenException("You are not authorized to update this restaurant");
        }
        
        restaurantMapper.updateEntityFromDto(requestDto, restaurant);
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        
        return restaurantMapper.toDto(updatedRestaurant);
    }
    
    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Only admin can delete
        if (!isAdmin(currentUser)) {
            throw new ForbiddenException("Only administrators can delete restaurants");
        }
        
        restaurantRepository.delete(restaurant);
    }
    
    @Transactional
    public RestaurantResponseDto updateRestaurantStatus(Long id, Boolean active) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        UserPrincipal currentUser = getCurrentUser();
        
        // Only admin can enable/disable
        if (!isAdmin(currentUser)) {
            throw new ForbiddenException("Only administrators can change restaurant status");
        }
        
        restaurant.setActive(active);
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        
        return restaurantMapper.toDto(updatedRestaurant);
    }
    
    public RestaurantResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        return restaurantMapper.toDto(restaurant);
    }
    
    public List<RestaurantResponseDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
            .map(restaurantMapper::toDto)
            .collect(Collectors.toList());
    }
    
    public List<RestaurantResponseDto> getRestaurantsByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId).stream()
            .map(restaurantMapper::toDto)
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
