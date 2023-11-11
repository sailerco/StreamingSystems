package org.example.EventPrompts;

import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemImpl;

public class EventMovingItemCreated extends Event {
    public MovingItem item;

    public EventMovingItemCreated(String name, int[] location, int value) {
        super();
        this.item = new MovingItemImpl(name, location, value);
    }
}
