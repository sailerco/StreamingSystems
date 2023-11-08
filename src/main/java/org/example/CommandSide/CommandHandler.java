package org.example.CommandSide;

import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.EventPrompts.EventMovingItemChangedValue;
import org.example.EventPrompts.EventMovingItemCreated;
import org.example.EventPrompts.EventMovingItemDeleted;
import org.example.EventPrompts.EventMovingItemMoved;
import org.example.EventAPI.EventStoreImpl;

public class CommandHandler {

    EventStoreImpl _eventStore = new EventStoreImpl();
    // Singleton implementiert
    private static CommandHandler INSTANCE;
    public static CommandHandler getInstance(){
        if(INSTANCE == null)
            INSTANCE = new CommandHandler();
        return INSTANCE;
    }
    //TODO: Validierung
   public void handle(CommandCreateItem command){
       _eventStore.store(new EventMovingItemCreated(command.id, command.location, command.value));
    }
    public void handle(CommandMoveItem command){
        _eventStore.store(new EventMovingItemMoved(command.id, command.vector));
    }
    public void handle(CommandDeleteItem command){
        _eventStore.store(new EventMovingItemDeleted(command.id));
    }
    public void handle(CommandChangeValue command){
        _eventStore.store(new EventMovingItemChangedValue(command.id, command.newValue));
    }
}
