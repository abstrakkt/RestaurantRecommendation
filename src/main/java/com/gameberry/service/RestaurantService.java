package com.gameberry.service;

import com.gameberry.model.Restaurant;

import java.util.*;

public class RestaurantService {

    private static final int TOP_RESTAURANT_COUNT = 4;

    public Set<Restaurant> getTopNewlyCreatedRestaurants(Restaurant[] availableRestaurants) {
        Set<Restaurant> newRestaurantSet = new HashSet<>();

        PriorityQueue<Restaurant> queue = new PriorityQueue<>(Comparator.comparingDouble(Restaurant::getRating));
        Collections.addAll(queue, availableRestaurants);

        int counter = 1;
        Date fortyEightHrsAgo = new Date(System.currentTimeMillis() - (48 * 60 * 60 * 1000));

        while (!queue.isEmpty() && counter <= TOP_RESTAURANT_COUNT) {
            Restaurant tempRestaurant = queue.poll();
            if (!newRestaurantSet.contains(tempRestaurant) && tempRestaurant.getOnboardedTime().after(fortyEightHrsAgo)) {
                newRestaurantSet.add(tempRestaurant);
                counter++;
            }
        }

        return newRestaurantSet;
    }
}
