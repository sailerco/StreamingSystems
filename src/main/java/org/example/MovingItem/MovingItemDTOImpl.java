package org.example.MovingItem;

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
}
