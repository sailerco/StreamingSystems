package org.example.EventPrompts;

public class EventMovingItemChangedValue extends Event {
    public int newValue;

    public EventMovingItemChangedValue(String id, int newValue) {
        this.id = id;
        this.newValue = newValue;
    }
}
