package com.gameberry;

import com.gameberry.model.*;
import com.gameberry.service.RestaurantService;
import com.gameberry.service.UserService;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final int RECOMMENDED_RESTAURANT_LIST_SIZE = 100;

    private final UserService userService;
    private final RestaurantService restaurantService;

    public Main(UserService userService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    public String[] getRestaurantRecommendations(User user, Restaurant[] availableRestaurants) {
        Map<Integer, CuisineTracking> topCuisines = userService.getTopCuisines(user.getCuisines());
        Cuisine primaryCuisine = Cuisine.valueOf(topCuisines.get(1).getType());
        Cuisine firstSecondaryCuisine = Cuisine.valueOf(topCuisines.get(2).getType());
        Cuisine secondSecondaryCuisine = Cuisine.valueOf(topCuisines.get(3).getType());

        Map<Integer, CostTracking> topCostBrackets = userService.getTopCostBrackets(user.getCostBracket());
        int primaryCostBracket = Integer.parseInt(topCostBrackets.get(1).getType());
        int firstSecondaryCostBracket = Integer.parseInt(topCostBrackets.get(2).getType());
        int secondSecondaryCostBracket = Integer.parseInt(topCostBrackets.get(3).getType());

        Set<Restaurant> recommendedRestaurants = new LinkedHashSet<>();

        // Step 1: Featured restaurants of primary cuisine and primary cost bracket.
        // If none, then all featured restaurants of primary cuisine, secondary cost and secondary cuisine, primary cost
        for (Restaurant restaurant : availableRestaurants) {
            // featured restaurant and primary cuisine and primary cost bracket
            if (restaurant.isRecommended()
                    && restaurant.getCuisine().equals(primaryCuisine)
                        && restaurant.getCostBracket() == primaryCostBracket) {
                recommendedRestaurants.add(restaurant);
            }
        }

        if (recommendedRestaurants.isEmpty()) {
            for (Restaurant restaurant : availableRestaurants) {
                if (restaurant.isRecommended()
                        && ((restaurant.getCuisine().equals(primaryCuisine)
                                && (restaurant.getCostBracket() == firstSecondaryCostBracket
                                        || restaurant.getCostBracket() == secondSecondaryCostBracket))
                        || ((restaurant.getCuisine().equals(firstSecondaryCuisine)
                                || restaurant.getCuisine().equals(secondSecondaryCuisine))
                            && restaurant.getCostBracket() == primaryCostBracket))) {
                    recommendedRestaurants.add(restaurant);
                }
            }
        }

        // Step 2: All restaurants of Primary cuisine, primary cost bracket with rating >= 4
        for (Restaurant restaurant : availableRestaurants) {
            if (restaurant.getCuisine().equals(primaryCuisine)
                    && restaurant.getCostBracket() == primaryCostBracket
                    && restaurant.getRating() >= 4) {
                recommendedRestaurants.add(restaurant);
            }
        }

        // Step 3: All restaurants of Primary cuisine, secondary cost bracket with rating >= 4.5
        for (Restaurant restaurant : availableRestaurants) {
            if (restaurant.getCuisine().equals(primaryCuisine)
                    && (restaurant.getCostBracket() == firstSecondaryCostBracket
                            || restaurant.getCostBracket() == secondSecondaryCostBracket)
                    && restaurant.getRating() >= 4.5) {
                recommendedRestaurants.add(restaurant);
            }
        }

        // Step 4: All restaurants of secondary cuisine, primary cost bracket with rating >= 4.5
        for (Restaurant restaurant : availableRestaurants) {
            if ((restaurant.getCuisine().equals(firstSecondaryCuisine)
                    || restaurant.getCuisine().equals(secondSecondaryCuisine))
                && restaurant.getCostBracket() == primaryCostBracket
                && restaurant.getRating() >= 4.5) {
                recommendedRestaurants.add(restaurant);
            }
        }

        // Step 5: Top 4 newly created restaurants by rating
        Set<Restaurant> topNewRestaurants = restaurantService.getTopNewlyCreatedRestaurants(availableRestaurants);
        recommendedRestaurants.addAll(topNewRestaurants);

        // Step 6: All restaurants of Primary cuisine, primary cost bracket with rating < 4
        for (Restaurant restaurant : availableRestaurants) {
            if (restaurant.getCuisine().equals(primaryCuisine)
                    && restaurant.getCostBracket() == primaryCostBracket
                    && restaurant.getRating() < 4) {
                recommendedRestaurants.add(restaurant);
            }
        }

        // Step 7: All restaurants of Primary cuisine, secondary cost bracket with rating < 4.5
        for (Restaurant restaurant : availableRestaurants) {
            if (restaurant.getCuisine().equals(primaryCuisine)
                    && (restaurant.getCostBracket() == firstSecondaryCostBracket || restaurant.getCostBracket() == secondSecondaryCostBracket)
                    && restaurant.getRating() < 4.5) {
                recommendedRestaurants.add(restaurant);
            }
        }

        // Step 8: All restaurants of secondary cuisine, primary cost bracket with rating < 4.5
        for (Restaurant restaurant : availableRestaurants) {
            if ((restaurant.getCuisine().equals(firstSecondaryCuisine) || restaurant.getCuisine().equals(secondSecondaryCuisine))
                    && restaurant.getCostBracket() == primaryCostBracket
                    && restaurant.getRating() < 4.5) {
                recommendedRestaurants.add(restaurant);
            }
        }

        // Step 9: All restaurants of any cuisine, any cost bracket
        recommendedRestaurants.addAll(Arrays.asList(availableRestaurants));

        // Convert recommended restaurants to an array of strings
        String[] recommendedRestaurantNames = new String[RECOMMENDED_RESTAURANT_LIST_SIZE];

        int i = 0;
        for (Restaurant tempRestuarant: recommendedRestaurants) {
            recommendedRestaurantNames[i++] = tempRestuarant.getRestaurantId();
            if (i >= RECOMMENDED_RESTAURANT_LIST_SIZE)
                break;
        }

        return recommendedRestaurantNames;
    }
}
