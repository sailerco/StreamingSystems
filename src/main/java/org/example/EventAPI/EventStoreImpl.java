package org.example.EventAPI;

import org.example.EventPrompts.Event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventStoreImpl implements EventStore {
    public BlockingQueue<Event> queue = new LinkedBlockingDeque<>();

    //Saves the given event in the blocking queue. If the saving operation is successful,
    //it triggers the event handler to consume the event, allowing further processing.
    public void store(Event event) {
        if (queue.offer(event)) {
            new EventHandler().consumeEvent(event);
        }
    }
}
