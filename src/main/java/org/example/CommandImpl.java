package org.example;

public class CommandImpl implements Commands{

    @Override
    public void createItem(String id) {
        CommandHandler.getInstance().handle(new CommandCreateItem(id));
    }

    @Override
    public void createItem(String id, int[] position, int value) {
        CommandHandler.getInstance().handle(new CommandCreateItem(id, position, value));
    }

    @Override
    public void deleteItem(String id) {
        CommandHandler.getInstance().handle(new CommandDeleteItem(id));
    }

    @Override
    public void moveItem(String id, int[] vector) {
        CommandHandler.getInstance().handle(new CommandMoveItem(id, vector));
    }

    @Override
    public void changeValue(String id, int newValue) {
        CommandHandler.getInstance().handle(new CommandChangeValue(id, newValue));
    }
}
