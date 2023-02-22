package com.gameberry.model;

import lombok.Data;


@Data
public class User {
    private CuisineTracking[] cuisines;
    private CostTracking[] costBracket;
}
