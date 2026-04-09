package com.codedecode.restuarantlisting.service;

import com.codedecode.restuarantlisting.dto.RestaurantDTO;
import com.codedecode.restuarantlisting.entity.Restaurant;
import com.codedecode.restuarantlisting.repository.RestaurantRepository;
import com.codedecode.restuarantlisting.sevice.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    private ModelMapper mapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(restaurantRepository, mapper);
    }

    @Test
    void testGetAllRestaurants() {
        List<Restaurant> restaurantList = Arrays.asList(
                new Restaurant(1L, "Restaurant A", "Address A", "Cuisine A", "Description A"),
                new Restaurant(2L, "Restaurant B", "Address B", "Cuisine B", "Description B")
        );

        when(restaurantRepository.findAll()).thenReturn(restaurantList);
        List<RestaurantDTO> restaurantDTOS = restaurantService.getAllRestaurants();

        assertNotNull(restaurantDTOS);
        assertEquals(restaurantList.size(), restaurantDTOS.size());
        for (int i=0; i<restaurantList.size(); i++) {
            assertEquals(restaurantList.get(i).getName(), restaurantDTOS.get(i).getName());
            assertEquals(restaurantList.get(i).getAddress(), restaurantDTOS.get(i).getAddress());
            assertEquals(restaurantList.get(i).getCity(), restaurantDTOS.get(i).getCity());
            assertEquals(restaurantList.get(i).getRestaurantDescription(), restaurantDTOS.get(i).getRestaurantDescription());
        }

        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testAddRestaurant() {
        RestaurantDTO restaurantDTO = new RestaurantDTO(null, "Restaurant A",
                "Address A", "Cuisine A", "Description A");
        Restaurant restaurant = new Restaurant(1L, "Restaurant A",
                "Address A", "Cuisine A", "Description A");

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        RestaurantDTO addedRestaurant = restaurantService.addRestaurant(restaurantDTO);

        assertEquals(restaurant.getName(), addedRestaurant.getName());
        assertEquals(restaurant.getAddress(), addedRestaurant.getAddress());
        assertEquals(restaurant.getCity(), addedRestaurant.getCity());
        assertEquals(restaurant.getRestaurantDescription(), addedRestaurant.getRestaurantDescription());

        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testFindRestaurantById() {
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant(restaurantId, "Restaurant A",
                "Address A", "Cuisine A", "Description A");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        RestaurantDTO foundRestaurant = restaurantService.findRestaurantById(restaurantId);

        assertEquals(restaurant.getName(), foundRestaurant.getName());
        assertEquals(restaurant.getAddress(), foundRestaurant.getAddress());
        assertEquals(restaurant.getCity(), foundRestaurant.getCity());
        assertEquals(restaurant.getRestaurantDescription(), foundRestaurant.getRestaurantDescription());

        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    @Test
    void testFindRestaurantById_NotFound() {
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        try {
            restaurantService.findRestaurantById(restaurantId);
        } catch (RuntimeException e) {
            assertEquals("Restaurant not found with id: " + restaurantId, e.getMessage());
        }

        verify(restaurantRepository, times(1)).findById(restaurantId);
    }
}
