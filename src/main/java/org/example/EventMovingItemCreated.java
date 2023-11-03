package org.example;

public class EventMovingItemCreated extends Event{
    private String name;
    private int[] location;
    private int numberOfMoves;
    private int value;

    public EventMovingItemCreated(String name, int[] location, int numberOfMoves, int value) {
        super();
        this.name = name;
        this.location = location;
        this.numberOfMoves = numberOfMoves;
        this.value = value;
    }
}
