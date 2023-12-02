package org.example.MovingItem;

import java.util.Arrays;
import java.util.Objects;

public class MovingItemDTOImpl implements MovingItemDTO {
    private final String name;
    private int[] location;
    private int moves;
    private int value;

    public MovingItemDTOImpl(String name, int[] location, int moves, int value) {
        this.name = name;
        this.location = location;
        this.moves = moves;
        this.value = value;
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
    public void setLocation(int[] location) {
        this.location = location;
    }

    @Override
    public int getNumberOfMoves() {
        return moves;
    }

    @Override
    public void setMoves() {
        this.moves = moves + 1;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovingItemDTOImpl that = (MovingItemDTOImpl) o;
        return moves == that.moves && value == that.value && Objects.equals(name, that.name) && Arrays.equals(location, that.location);
    }

}
