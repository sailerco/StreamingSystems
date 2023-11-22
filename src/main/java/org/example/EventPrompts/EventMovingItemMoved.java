package org.example.EventPrompts;

public class EventMovingItemMoved extends EventVector {
    public String id;

    public EventMovingItemMoved(String id, int[] vector) {
        this.id = id;
        this.vector = vector;
    }
}
