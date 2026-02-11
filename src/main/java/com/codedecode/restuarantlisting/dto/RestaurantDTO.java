package com.codedecode.restuarantlisting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO  {
    Long id;
    String name;
    String address;
    String city;
    String restaurantDescription;
}
