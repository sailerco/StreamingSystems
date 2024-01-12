package org.example.EventAPI;

import org.example.Consumer;
import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.QuerySide.QueryModel.query_database;

//projects data to the query model
public class EventHandler {
    private final List<Long> eventTimes = new ArrayList<>();
    private final Consumer consumer = new Consumer();

    public void processMessage() throws jakarta.jms.JMSException {
        Map<Event, Long> events = consumer.getEventWithTimestamp(100);
        for (Map.Entry<Event, Long> entry : events.entrySet()) {
            eventTimes.add(System.currentTimeMillis() - entry.getValue());
            consumeEvent(entry.getKey());
        }
    }

    public void consumeEvent(Event event) {
        if (event instanceof EventMovingItemCreated) {
            MovingItem item = ((EventMovingItemCreated) event).item;
            MovingItemDTO itemDTO = new MovingItemDTOImpl(item.getName(), item.getLocation(), item.getNumberOfMoves(), item.getValue());
            query_database.put(((EventMovingItemCreated) event).item.getName(), itemDTO);
        } else if (event instanceof EventMovingItemMoved) {
            movePosition(query_database.get(event.id), (EventVector) event);
        } else if (event instanceof EventMovingItemDeleted) {
            query_database.remove(event.id);
        } else if (event instanceof EventMovingItemChangedValue) {
            MovingItemDTO itemDTO = query_database.get(event.id);
            itemDTO.setValue(((EventMovingItemChangedValue) event).newValue);
        } else if (event instanceof EventDeleteItemAndMoveAnotherItem) {
            query_database.remove(event.id);
            MovingItemDTO itemDTO = query_database.get(((EventDeleteItemAndMoveAnotherItem) event).new_id);
            movePosition(itemDTO, (EventVector) event);
        } else if (event instanceof EventMovingItemCreatedOnUsedPosition) {
            query_database.remove(event.id);
            MovingItem item = ((EventMovingItemCreatedOnUsedPosition) event).item;
            MovingItemDTO itemDTO = new MovingItemDTOImpl(item.getName(), item.getLocation(), item.getNumberOfMoves(), item.getValue());
            query_database.put(((EventMovingItemCreatedOnUsedPosition) event).item.getName(), itemDTO);
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