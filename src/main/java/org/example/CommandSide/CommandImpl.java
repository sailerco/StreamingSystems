package org.example.CommandSide;

import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;

public class CommandImpl implements Commands {

    @Override
    public void createItem(String id) throws Exception {
        CommandHandler.getInstance().handle(new CommandCreateItem(id));
    }

    @Override
    public void createItem(String id, int[] position, int value) throws Exception {
        CommandHandler.getInstance().handle(new CommandCreateItem(id, position, value));
    }

    @Override
    public void deleteItem(String id) throws Exception {
        CommandHandler.getInstance().handle(new CommandDeleteItem(id));
    }

    @Override
    public void moveItem(String id, int[] vector) throws Exception {
        CommandHandler.getInstance().handle(new CommandMoveItem(id, vector));
    }

    @Override
    public void changeValue(String id, int newValue) throws Exception {
        CommandHandler.getInstance().handle(new CommandChangeValue(id, newValue));
    }
}
