package org.example.QuerySide;

import org.example.MovingItem.MovingItemDTO;

import java.util.HashMap;
import java.util.Map;


public class QueryModel {
    //Represents a database of MovingItemDTO objects accessible for querying. The Event Handler saves items into this database, and the query side can retrieve items from it.
    public static Map<String, MovingItemDTO> query_database = new HashMap<>();
}
