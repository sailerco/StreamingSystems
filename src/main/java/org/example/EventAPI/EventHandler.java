package org.example.EventAPI;

import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;
import org.example.MovingItem.MovingItemImpl;

import static org.example.QuerySide.QueryModel.query_database;

//projeziert daten auf das Query Model
public class EventHandler {
    EventStore eventStore;
    public void consumeEvent(Event event) {
        //Event event = eventStore.getEvent();
        if (event instanceof EventMovingItemCreated) {
            MovingItemImpl itemCommand = ((EventMovingItemCreated) event).item;
            MovingItemDTO itemQuery = new MovingItemDTOImpl(itemCommand.getName(), itemCommand.getLocation(), itemCommand.getNumberOfMoves(), itemCommand.getValue());
            query_database.put(((EventMovingItemCreated) event).item.getName(), itemQuery);
        } else if (event instanceof EventMovingItemMoved) {
            MovingItemDTO item = query_database.get(((EventMovingItemMoved) event).id);
            int[] location = item.getLocation();
            for (int i = 0; i < location.length; i++) {
                location[i] += ((EventMovingItemMoved) event).vector[i];
            }
            item.setLocation(location);
            item.setMoves();
        } else if (event instanceof EventMovingItemDeleted) {
            query_database.remove(((EventMovingItemDeleted) event).id);
        } else if (event instanceof EventMovingItemChangedValue) {
            MovingItemDTO item = query_database.get(((EventMovingItemChangedValue) event).id);
            item.setValue(((EventMovingItemChangedValue) event).newValue);
        }
    }
}