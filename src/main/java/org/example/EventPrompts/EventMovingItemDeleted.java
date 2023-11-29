package org.example.EventPrompts;

import java.io.Serializable;

public class EventMovingItemDeleted extends Event implements Serializable {

    public EventMovingItemDeleted(String id) {
        this.id = id;
    }
}
