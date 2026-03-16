package com.codedecode.restuarantlisting.controller;

import com.codedecode.restuarantlisting.dto.RestaurantDTO;
import com.codedecode.restuarantlisting.sevice.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestaurantControllerTest {

    @InjectMocks
    RestaurantController restaurantController;

    @Mock
    RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRestaurants() {
        // Implement test logic for getAllRestaurants method
        List<RestaurantDTO> mockRestaurants = Arrays.asList(
                new RestaurantDTO(1L, "Restaurant A", "Address A", "Cuisine A", "Description A"),
                new RestaurantDTO(2L, "Restaurant B", "Address B", "Cuisine B", "Description B")
        );
        when(restaurantService.getAllRestaurants()).thenReturn(mockRestaurants);

        ResponseEntity<List<RestaurantDTO>> allRestaurants = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.OK, allRestaurants.getStatusCode());
        assertEquals(2, allRestaurants.getBody().size());
        assertEquals(mockRestaurants, allRestaurants.getBody());

        //Verify that the service method was called
        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    void testAddRestaurant() {
        // Implement test logic for addRestaurant method
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(null, "Restaurant A", "Address A", "Cuisine A", "Description A");
        when(restaurantService.addRestaurant(mockRestaurantDTO)).thenReturn(mockRestaurantDTO);

        ResponseEntity<RestaurantDTO> restaurantResponse = restaurantController.addRestaurant(mockRestaurantDTO);

        assertEquals(HttpStatus.CREATED, restaurantResponse.getStatusCode());
        assertEquals(mockRestaurantDTO, restaurantResponse.getBody());

        //Verify that the service method was called
        verify(restaurantService, times(1)).addRestaurant(mockRestaurantDTO);
    }

    @Test
    void testfindRestaurantById() {
        // Implement test logic for findRestaurantById method
        Long restaurantId = 1L;
        RestaurantDTO mockRestaurant = new RestaurantDTO(restaurantId, "Restaurant A", "Address A", "Cuisine A", "Description A");
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(mockRestaurant);

        ResponseEntity<RestaurantDTO> restaurantResponse = restaurantController.findRestaurantById(restaurantId);

        assertEquals(HttpStatus.OK, restaurantResponse.getStatusCode());
        assertEquals(mockRestaurant, restaurantResponse.getBody());

        //Verify that the service method was called
        verify(restaurantService, times(1)).findRestaurantById(restaurantId);
    }
}
