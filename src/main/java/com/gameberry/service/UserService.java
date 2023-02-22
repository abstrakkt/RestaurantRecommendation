package com.gameberry.service;

import com.gameberry.model.CostTracking;
import com.gameberry.model.CuisineTracking;

import java.util.*;

public class UserService {

    private static final int TOP_LIST_COUNT = 3;

    public Map<Integer, CostTracking> getTopCostBrackets(CostTracking[] costBracketArr) {
        PriorityQueue<CostTracking> queue = new PriorityQueue<>(Comparator.comparingInt((CostTracking obj) -> Integer.parseInt(obj.getNoOfOrders())).reversed());
        queue.addAll(Arrays.asList(costBracketArr));
        Map<Integer, CostTracking> topCostBrackets = new HashMap<>();
        for (int i = 0; i < TOP_LIST_COUNT; i++) {
            topCostBrackets.put(i + 1, queue.poll());
        }

        return topCostBrackets;
    }

    public Map<Integer, CuisineTracking> getTopCuisines(CuisineTracking[] cuisinesArr) {
        PriorityQueue<CuisineTracking> queue = new PriorityQueue<>(Comparator.comparingInt((CuisineTracking obj) -> Integer.parseInt(obj.getNoOfOrders())).reversed());
        queue.addAll(Arrays.asList(cuisinesArr));
        Map<Integer, CuisineTracking> topCuisineBrackets = new HashMap<>();
        for (int i = 0; i < TOP_LIST_COUNT; i++) {
            topCuisineBrackets.put(i + 1, queue.poll());
        }

        return topCuisineBrackets;
    }
}
