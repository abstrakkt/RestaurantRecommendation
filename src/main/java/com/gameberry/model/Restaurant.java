package com.gameberry.model;

import lombok.Data;

import java.util.Date;

@Data
public class Restaurant {

    private String restaurantId;
    private Cuisine cuisine;
    private int costBracket;
    private float rating;
    private boolean isRecommended;
    private Date onboardedTime;

}
