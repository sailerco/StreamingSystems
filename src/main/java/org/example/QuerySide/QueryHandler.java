package org.example.QuerySide;

import org.example.MovingItem.MovingItemDTO;

import java.util.*;

import static org.example.QuerySide.QueryModel.query_database;

public class QueryHandler implements Query {
    @Override
    public MovingItemDTO getMovingItemByName(String name) {
        if (query_database.get(name) != null)
            return query_database.get(name);
        else
            return null;
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItems() {
        List<MovingItemDTO> items = new ArrayList<>();
        for (Map.Entry<String, MovingItemDTO> entry : query_database.entrySet()) {
            items.add(entry.getValue());
        }
        return Collections.enumeration(items);
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItemsAtPosition(int[] position) {
        List<MovingItemDTO> items = new ArrayList<>();
        for (Map.Entry<String, MovingItemDTO> entry : query_database.entrySet()) {
            if (Arrays.equals(entry.getValue().getLocation(), position)) {
                items.add(entry.getValue());
            }
        }
        return Collections.enumeration(items);
    }
}