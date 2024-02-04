package org.example.EventAPI;

import jakarta.jms.JMSException;
import jakarta.jms.ObjectMessage;
import org.example.ConnectionMQ;
import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;

import java.util.ArrayList;
import java.util.List;

import static org.example.QuerySide.QueryModel.query_database;

//projects data to the query model
public class EventHandler {
    static ConnectionMQ connectionMQ;
    private final List<Long> eventTimes = new ArrayList<>();

    public EventHandler() throws JMSException {
        connectionMQ = new ConnectionMQ("consumer");
    }

    static public void stop() throws JMSException {
        connectionMQ.close();
    }

    public void processMessage() throws JMSException {
        ObjectMessage eventMessage = (ObjectMessage) connectionMQ.consumeMessage();
        if (eventMessage.getObject() instanceof Event) {
            eventTimes.add((System.currentTimeMillis() - eventMessage.getJMSTimestamp()));
            consumeEvent((Event) eventMessage.getObject());
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