package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.dto.RestaurantRequestDto;
import com.fooddelivery.restaurant.dto.RestaurantResponseDto;
import com.fooddelivery.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    
    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(
            @Valid @RequestBody RestaurantRequestDto requestDto) {
        RestaurantResponseDto response = restaurantService.createRestaurant(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequestDto requestDto) {
        RestaurantResponseDto response = restaurantService.updateRestaurant(id, requestDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok(Map.of("message", "Restaurant deleted successfully"));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantResponseDto> updateRestaurantStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> statusUpdate) {
        Boolean active = statusUpdate.get("active");
        RestaurantResponseDto response = restaurantService.updateRestaurantStatus(id, active);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
        RestaurantResponseDto response = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants() {
        List<RestaurantResponseDto> response = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurantsByOwner(@PathVariable Long ownerId) {
        List<RestaurantResponseDto> response = restaurantService.getRestaurantsByOwner(ownerId);
        return ResponseEntity.ok(response);
    }
}
