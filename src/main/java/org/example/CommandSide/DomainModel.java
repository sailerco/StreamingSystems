package org.example.CommandSide;

import org.example.EventAPI.EventStoreImpl;
import org.example.EventPrompts.Event;

import java.util.ArrayList;
import java.util.List;

public class DomainModel {
    public static List<String> domainIDs = new ArrayList<>();
    EventStoreImpl eventStore = new EventStoreImpl();
    public Boolean exists(String id) {
        return domainIDs.contains(id);
    }

    public void add(String id) {
        domainIDs.add(id);
    }

    public void remove(String id) {
        domainIDs.remove(id);
    }

    public void storeEvent(Event event){
        eventStore.store(event);
    }
}
