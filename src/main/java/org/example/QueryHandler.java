package org.example;

import java.util.Enumeration;

public class QueryHandler implements Query{
    @Override
    public MovingItemDTO getMovingItemByName(String name) {
        return null;
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItems() {
        return null;
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItemsAtPosition(int[] position) {
        return null;
    }
}