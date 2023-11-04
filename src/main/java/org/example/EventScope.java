package org.example;

import org.example.EventPrompts.Event;

public class EventScope {
    Class<? extends Event> _eventType;
    String _id;

    public EventScope(Class<? extends Event> eventType, String id) {
        this._eventType = eventType;
        this._id = id;
    }
}
