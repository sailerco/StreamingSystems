package org.example.CommandPrompts;

public class CommandMoveItem extends CommandPrompt{
    public int[] vector;

    public CommandMoveItem(String id, int[] vector) {
        this.id = id;
        this.vector = vector;
    }

}
