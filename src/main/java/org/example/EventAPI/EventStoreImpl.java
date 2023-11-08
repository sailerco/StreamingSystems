package org.example.EventAPI;

import org.example.EventPrompts.Event;
import org.example.ItemAggregate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//speichert Events ab in der Queue
public class EventStoreImpl implements EventStore {
    public BlockingQueue<Event> queue = new LinkedBlockingDeque<>();

    public void store(Event event){
        if(queue.offer(event)){
            new EventHandler().consumeEvent(event);
        }
    }
    public Event getEvent() throws InterruptedException {
        return queue.take();
    }

    //todo: probs dann überflüssig
    public Object loadAggregate(Class<ItemAggregate> itemAggregateClass, Event event) {

        return new ItemAggregate();
    }
}
