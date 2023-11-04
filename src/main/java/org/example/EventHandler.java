package org.example;

import org.example.EventPrompts.*;

//projeziert daten auf das Query Model
public class EventHandler {
    EventStore eventStore;
    QueryModel model;
    public void consumeEvent(Event event){
        //Event event = eventStore.getEvent();
        if(event instanceof EventMovingItemCreated){
            //wir holen aus dem Event uns das item (also Moving Item), davon einerseits den String und andererseit das komplette Objekt.
            model.query_database.put(((EventMovingItemCreated) event).item.getName(), ((EventMovingItemCreated) event).item);
        }else if(event instanceof EventMovingItemMoved){
            // todo: impl oder nur interface hier nutzen
            MovingItemImpl item = model.query_database.get(((EventMovingItemMoved) event).id);
            int[] location = item.getLocation();
            for (int i = 0; i < location.length; i++) {
                location[i] += ((EventMovingItemMoved) event).vector[i];
            }
            item.setLocation(location);
            item.setMoves();
            //TODO: does it save automatically?
        }else if(event instanceof EventMovingItemDeleted){
            model.query_database.remove(((EventMovingItemDeleted) event).id);
        }else if (event instanceof EventMovingItemChangedValue){
            MovingItemImpl item = model.query_database.get(((EventMovingItemChangedValue) event).id);
            item.setValue(((EventMovingItemChangedValue) event).newValue);
        }
    }
}