package org.example.EventPrompts;

import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemImpl;

public class EventMovingItemCreatedOnUsedPosition extends Event{
    public MovingItem item;
    public String new_id;
    public EventMovingItemCreatedOnUsedPosition(String id, String new_id, int[] location, int value){
        this.id = id;
        this.new_id = new_id;
        this.item = new MovingItemImpl(new_id, location, value);
    }
}
