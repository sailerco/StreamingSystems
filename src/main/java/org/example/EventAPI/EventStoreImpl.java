package org.example.EventAPI;

import org.example.EventPrompts.Event;

import jakarta.jms.JMSException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//speichert Events ab in der Queue
public class EventStoreImpl implements EventStore {
    public BlockingQueue<Event> queue = new LinkedBlockingDeque<>();

    public void store(Event event) throws JMSException {
        if (queue.offer(event)) {
            new EventHandler().consumeEvent(event);
        }
    }

    public Event getEvent() throws InterruptedException {
        return queue.take();
    }
}
