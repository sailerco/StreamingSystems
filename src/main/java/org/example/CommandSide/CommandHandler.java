package org.example.CommandSide;

import org.example.CommandPrompts.*;
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
        model.create(command);
    }

    public void handle(CommandMoveItem command) {
        model.moveItem(command);
    }

    public void handle(CommandChangeValue command) {
        model.changeValue(command);
    }
    public void handle(CommandDeleteItem command) {
        model.remove(command);
    }
}
