package org.example.MovingItem;

import java.io.Serializable;

public class MovingItemImpl implements MovingItem, Serializable {
    private final String name;
    private int[] location = new int[3];
    private int moves;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    @Override
    public int getNumberOfMoves() {
        return moves;
    }

    @Override
    public int getValue() {
        return value;
    }
}

