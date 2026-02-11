package com.codedecode.restuarantlisting.sevice;

import com.codedecode.restuarantlisting.dto.RestaurantDTO;
import com.codedecode.restuarantlisting.entity.Restaurant;
import com.codedecode.restuarantlisting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper mapper;  // ✅ Spring injects this

    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return restaurantList.stream()
                .map(restaurant -> mapper.map(restaurant, RestaurantDTO.class)).toList();
    }

    public RestaurantDTO addRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository
                .save(mapper.map(restaurantDTO, Restaurant.class));
        return mapper.map(restaurant, RestaurantDTO.class);
    }

    public RestaurantDTO findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurant -> mapper.map(restaurant, RestaurantDTO.class))
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }
}
