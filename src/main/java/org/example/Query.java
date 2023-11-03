package org.example;

import java.util.Enumeration;

public interface Query {
    public MovingItemDTO getMovingItemByName(String name);
    public Enumeration<MovingItemDTO> getMovingItems();
    public Enumeration<MovingItemDTO> getMovingItemsAtPosition(int[] position);
}
