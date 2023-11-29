package org.example.EventPrompts;

import java.io.Serializable;

public class EventMovingItemChangedValue extends Event implements Serializable {
    public int newValue;

    public EventMovingItemChangedValue(String id, int newValue) {
        this.id = id;
        this.newValue = newValue;
    }
}
