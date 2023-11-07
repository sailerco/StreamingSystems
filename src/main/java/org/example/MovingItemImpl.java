package org.example;

public class MovingItemImpl implements MovingItem {
    private final String name;
    private int[] location = new int[3];
    public int moves;
    private int value;

    public MovingItemImpl(String name, int[] location, int value) {
        this.name = name;
        this.location = location;
        this.value = value;
        this.moves = 0;
    }

    public MovingItemImpl(String name, int value) {
        this.name = name;
        this.value = value;
        this.location = new int[]{0, 0, 0};
        this.moves = 0;
    }

    public void setMoves() {
        this.moves = moves + 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    @Override
    public int getNumberOfMoves() {
        return moves;
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

