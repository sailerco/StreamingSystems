package org.example.QuerySide;

import org.example.MovingItem.MovingItemDTO;

import java.util.Enumeration;

public interface Query {
    MovingItemDTO getMovingItemByName(String name);

    Enumeration<MovingItemDTO> getMovingItems();

    Enumeration<MovingItemDTO> getMovingItemsAtPosition(int[] position);
}
