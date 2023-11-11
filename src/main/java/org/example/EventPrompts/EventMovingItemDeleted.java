package org.example.EventPrompts;

public class EventMovingItemDeleted extends Event {
    public String id;

    public EventMovingItemDeleted(String id) {
        this.id = id;
    }
}
