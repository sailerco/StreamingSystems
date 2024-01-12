package org.example.EventAPI;

import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItem;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;

import static org.example.QuerySide.QueryModel.query_database;

//projeziert daten auf das Query Model
public class EventHandler {
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
}