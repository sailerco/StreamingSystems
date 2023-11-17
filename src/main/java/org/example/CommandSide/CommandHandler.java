package org.example.CommandSide;

import org.example.CommandPrompts.*;
import org.example.EventAPI.EventStoreImpl;
import org.example.EventPrompts.*;
public class CommandHandler {
    DomainModel model = new DomainModel();

    // Singleton implementiert
    private static CommandHandler INSTANCE;

    public static CommandHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CommandHandler();
        return INSTANCE;
    }

    public void handle(CommandCreateItem command) {
        if (!model.exists(command.id)) {
            model.add(command.id);
            model.storeEvent(new EventMovingItemCreated(command.id, command.location, command.value));
        } else
            System.out.println("Item with id " + command.id + " already exists");
    }

    public void handle(CommandMoveItem command) {
        if (model.exists(command.id))
            model.storeEvent(new EventMovingItemMoved(command.id, command.vector));
        else
            System.out.println("Item with id " + command.id + "doesn't exist");
    }

    public void handle(CommandDeleteItem command) {
        if (model.exists(command.id)) {
            model.storeEvent(new EventMovingItemDeleted(command.id));
            model.remove(command.id);
        }
    }

    public void handle(CommandChangeValue command) {
        if (model.exists(command.id))
            model.storeEvent(new EventMovingItemChangedValue(command.id, command.newValue));
        else
            System.out.println("Item with id " + command.id + "doesn't exist");
    }
}
