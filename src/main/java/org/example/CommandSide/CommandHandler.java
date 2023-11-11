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
    DomainModel _model = new DomainModel();
    // Singleton implementiert
    private static CommandHandler INSTANCE;
    public static CommandHandler getInstance(){
        if(INSTANCE == null)
            INSTANCE = new CommandHandler();
        return INSTANCE;
    }
    //TODO: Validierung
   public void handle(CommandCreateItem command){
        if(!_model.exists(command.id)){
            _model.add(command.id);
            _eventStore.store(new EventMovingItemCreated(command.id, command.location, command.value));
        }else
            System.out.println("Item with id "+ command.id + " already exists");
    }
    public void handle(CommandMoveItem command){
        if(_model.exists(command.id))
            _eventStore.store(new EventMovingItemMoved(command.id, command.vector));
        else
            System.out.println("Item with id " + command.id + "doesn't exist");
    }
    public void handle(CommandDeleteItem command){
        if(_model.exists(command.id)){
            _eventStore.store(new EventMovingItemDeleted(command.id));
            _model.remove(command.id);
        }
    }
    public void handle(CommandChangeValue command){
        if(_model.exists(command.id))
            _eventStore.store(new EventMovingItemChangedValue(command.id, command.newValue));
        else
            System.out.println("Item with id " + command.id + "doesn't exist");
    }
}
