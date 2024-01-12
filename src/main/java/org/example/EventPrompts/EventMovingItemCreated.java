package org.example.EventPrompts;

import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemImpl;

public class EventMovingItemCreated extends Event {
    public MovingItem item;

    public EventMovingItemCreated(String id, int[] location, int value) {
        super();
        this.id = id;
        this.item = new MovingItemImpl(id, location, value);
    }
}
