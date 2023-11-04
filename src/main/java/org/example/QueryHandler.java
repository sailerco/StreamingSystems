package org.example;

import java.util.*;

import static org.example.QueryModel.query_database;

public class QueryHandler implements Query{
    @Override
    public MovingItemDTO getMovingItemByName(String name) {
        return (MovingItemDTO) query_database.get(name);
    }

    @Override
    public Enumeration<MovingItemDTO> getMovingItems() {
        //todo: Enum? nicht richtig nutzen
        List<MovingItemDTO> items = new ArrayList<>();
        for(Map.Entry<String, MovingItemImpl> entry: query_database.entrySet()){
            items.add((MovingItemDTO) entry.getValue());
        }
        return Collections.enumeration(items);
    }
    @Override
    public Enumeration<MovingItemDTO> getMovingItemsAtPosition(int[] position) {
        List<MovingItemDTO> items = new ArrayList<>();
        for(Map.Entry<String, MovingItemImpl> entry: query_database.entrySet()){
            //todo: geht == hier oder muss equals sein?
            if(entry.getValue().getLocation() == position)
                items.add((MovingItemDTO) entry.getValue());
        }
        return Collections.enumeration(items);
    }
}