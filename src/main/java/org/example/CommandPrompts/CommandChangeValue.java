package org.example.CommandPrompts;

public class CommandChangeValue extends CommandPrompt{
    public int newValue;

    public CommandChangeValue(String id, int newValue) {
        this.id = id;
        this.newValue = newValue;
    }
}
