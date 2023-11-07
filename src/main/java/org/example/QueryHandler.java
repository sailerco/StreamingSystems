package org.example;

import java.util.*;

import static org.example.QueryModel.query_database;

public class QueryHandler implements Query{
    @Override
    public MovingItemDTO getMovingItemByName(String name) {
        //todo: datatype MovingItemImpl
        MovingItemImpl movingItem = query_database.get(name);
        if(movingItem != null)
            return new MovingItemDTOImpl(movingItem.getName(), movingItem.getLocation(), movingItem.moves, movingItem.getValue());
        else
            return null;
    }

    //TODO: FIX OTHER METHODS
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