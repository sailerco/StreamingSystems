package org.example.EventPrompts;

public class EventMovingItemMoved extends Event {
    public String id;
    public int[] vector;

    public EventMovingItemMoved(String id, int[] vector) {
        this.id = id;
        this.vector = vector;
    }
}
