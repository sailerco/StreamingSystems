package org.example.EventPrompts;

import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemImpl;

import java.io.Serializable;

public class EventMovingItemCreated extends Event implements Serializable {
    public MovingItem item;

    public EventMovingItemCreated(String id, int[] location, int value) {
        super();
        this.id = id;
        this.item = new MovingItemImpl(id, location, value);
    }
}
