package org.example.QuerySide;

import org.example.MovingItem.MovingItemDTOImpl;

import java.util.HashMap;
import java.util.Map;


public class QueryModel {
    //saves the MovingItemDTO. The Event Handler saves it and the query side can get it
    //TODO: make it immutable and use movingItem instead of MovingItemImpl.
    public static Map<String, MovingItemDTOImpl> query_database = new HashMap<>();
}
