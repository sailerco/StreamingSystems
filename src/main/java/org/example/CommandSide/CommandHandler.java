package org.example.CommandSide;

import org.example.CommandPrompts.*;
import org.example.EventAPI.EventStoreImpl;
import org.example.EventPrompts.*;
public class CommandHandler {

    EventStoreImpl _eventStore = new EventStoreImpl();
    DomainModel _model = new DomainModel();

    // Singleton implementiert
    private static CommandHandler INSTANCE;

    public static CommandHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CommandHandler();
        return INSTANCE;
    }

    public void handle(CommandCreateItem command) {
        if (!_model.exists(command.id)) {
            _model.add(command.id);
            _eventStore.store(new EventMovingItemCreated(command.id, command.location, command.value));
        } else
            System.out.println("Item with id " + command.id + " already exists");
    }

    public void handle(CommandMoveItem command) {
        if (_model.exists(command.id))
            _eventStore.store(new EventMovingItemMoved(command.id, command.vector));
        else
            System.out.println("Item with id " + command.id + "doesn't exist");
    }

    public void handle(CommandDeleteItem command) {
        if (_model.exists(command.id)) {
            _eventStore.store(new EventMovingItemDeleted(command.id));
            _model.remove(command.id);
        }
    }

    public void handle(CommandChangeValue command) {
        if (_model.exists(command.id))
            _eventStore.store(new EventMovingItemChangedValue(command.id, command.newValue));
        else
            System.out.println("Item with id " + command.id + "doesn't exist");
    }
}
