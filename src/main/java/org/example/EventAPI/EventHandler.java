package org.example.EventAPI;

import org.example.Consumer;
import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;

import java.util.ArrayList;
import java.util.List;

import static org.example.QuerySide.QueryModel.query_database;

//projeziert daten auf das Query Model
public class EventHandler {
    private final List<Long> eventTimes = new ArrayList<>();
    private final Consumer consumer = new Consumer(false);

    public void processMessage() throws jakarta.jms.JMSException {
        //TODO: maybe implement that we get the timestamp.
        // Might have to change the getEvent Method or create a new one, because currently it just returns the events
        List<Event> events = consumer.getEvent(100);
        for (Event event : events) {
            consumeEvent(event);
        }
    }

    public void consumeEvent(Event event) {
        if (event instanceof EventMovingItemCreated) {
            MovingItem item = ((EventMovingItemCreated) event).item;
            MovingItemDTO itemDTO = new MovingItemDTOImpl(item.getName(), item.getLocation(), item.getNumberOfMoves(), item.getValue());
            query_database.put(((EventMovingItemCreated) event).item.getName(), itemDTO);
        } else if (event instanceof EventMovingItemMoved) {
            MovingItemDTO item = query_database.get(event.id);
            movePosition(item, (EventVector) event);
        } else if (event instanceof EventMovingItemDeleted) {
            query_database.remove(event.id);
        } else if (event instanceof EventMovingItemChangedValue) {
            MovingItemDTO item = query_database.get(event.id);
            item.setValue(((EventMovingItemChangedValue) event).newValue);
        } else if (event instanceof EventDeleteItemAndMoveAnotherItem) {
            query_database.remove(event.id);
            MovingItemDTO item = query_database.get(((EventDeleteItemAndMoveAnotherItem) event).new_id);
            movePosition(item, (EventVector) event);
        } else if (event instanceof EventMovingItemCreatedOnUsedPosition) {
            query_database.remove(event.id);
            MovingItem item = ((EventMovingItemCreatedOnUsedPosition) event).item;
            MovingItemDTO itemQuery = new MovingItemDTOImpl(item.getName(), item.getLocation(), item.getNumberOfMoves(), item.getValue());
            query_database.put(((EventMovingItemCreatedOnUsedPosition) event).item.getName(), itemQuery);
        }
    }

    private void movePosition(MovingItemDTO item, EventVector event) {
        int[] location = item.getLocation();
        for (int i = 0; i < location.length; i++) {
            location[i] += event.vector[i];
        }
        item.setLocation(location);
        item.setMoves();
    }

    public float calculateMeanTime() {
        if (eventTimes.isEmpty()) return 0;

        float total = 0;
        for (Long time : eventTimes)
            total += time;

        return total / eventTimes.size();
    }
}