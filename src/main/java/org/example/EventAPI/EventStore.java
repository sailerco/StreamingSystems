package org.example.EventAPI;

import org.example.EventPrompts.Event;

public interface EventStore {
    void store(Event event);
}