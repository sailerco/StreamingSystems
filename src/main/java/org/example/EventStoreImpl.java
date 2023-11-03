package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class EventStoreImpl implements EventStore {
    public BlockingQueue<Event> queue = new LinkedBlockingDeque<>();

    public void store(Event event){
        queue.offer(event);
    }
    public void getEvent(Event event) throws InterruptedException {
        // queue.take(event);
        if(queue.contains(event)){
            queue.take(event);
        }
    }
    public Object loadAggregate(Class<ItemAggregate> itemAggregateClass, Event event) {

        return new ItemAggregate();
    }
}
