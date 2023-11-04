package org.example;

import org.example.EventPrompts.Event;

public interface EventStore {
    public void store(Event event) throws InterruptedException;
    public Event getEvent() throws InterruptedException;
}
// Getter und Setter als Kapselung einsetzen
