package com.codedecode.restuarantlisting.controller;

import com.codedecode.restuarantlisting.dto.RestaurantDTO;
import com.codedecode.restuarantlisting.sevice.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin
public class RestaurantController {

    RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        return  new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> addRestaurant(RestaurantDTO restaurantDTO) {
        return new ResponseEntity<>(restaurantService.addRestaurant(restaurantDTO)
                , HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> findRestaurantById(@PathVariable Long id) {
        return new ResponseEntity<>(restaurantService.findRestaurantById(id), HttpStatus.OK);
    }
}
