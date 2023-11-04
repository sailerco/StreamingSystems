package org.example.CommandPrompts;

public class CommandCreateItem {
    public String id;
    public int[] location = new int[3];
    public int value;

    public CommandCreateItem(String id) {
        this.id = id;
        this.value = 0;
    }

    public CommandCreateItem(String id, int[] position, int value) {
        this.id = id;
        this.location = position;
        this.value = value;
    }
}
