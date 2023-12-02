package org.example.EventPrompts;

import java.io.Serializable;

public class EventMovingItemMoved extends EventVector implements Serializable {

    public EventMovingItemMoved(String id, int[] vector) {
        this.id = id;
        this.vector = vector;
    }
}
