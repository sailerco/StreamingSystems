package org.example.EventAPI;

import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import static org.example.QuerySide.QueryModel.query_database;

//projeziert daten auf das Query Model
public class EventHandler {
    ConsumerConnection consumerConnection;

    public EventHandler() throws JMSException {
        this.consumerConnection = new ConsumerConnection();
    }

    public void doEvent() throws JMSException {
        ObjectMessage eventMessage = (ObjectMessage) this.consumerConnection.consumeMessage();
        System.out.println(eventMessage.getObject());
        if (eventMessage.getObject() instanceof Event)
            consumeEvent((Event) eventMessage.getObject());

    }

    public void consumeEvent(Event event) {
        if (event instanceof EventMovingItemCreated) {
            MovingItem itemCommand = ((EventMovingItemCreated) event).item;
            MovingItemDTO itemQuery = new MovingItemDTOImpl(itemCommand.getName(), itemCommand.getLocation(), itemCommand.getNumberOfMoves(), itemCommand.getValue());
            query_database.put(((EventMovingItemCreated) event).item.getName(), itemQuery);
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
}