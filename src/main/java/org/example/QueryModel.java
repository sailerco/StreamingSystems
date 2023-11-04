package org.example;

import java.util.HashMap;
import java.util.Map;


public class QueryModel {
    //speichert unsere Moving Items. Werden im EventHandler geadded.
    //TODO: make it immutable and use movingItem instead of MovingItemImpl.
    public static Map<String, MovingItemImpl> query_database = new HashMap<>();
}
