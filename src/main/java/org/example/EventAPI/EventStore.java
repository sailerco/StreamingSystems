package org.example.EventAPI;

import org.example.EventPrompts.Event;

import jakarta.jms.JMSException;

public interface EventStore {
    void store(Event event) throws JMSException;

    Event getEvent() throws InterruptedException;
}