package org.example;

public class MovingItemDTOImpl implements MovingItemDTO {
    private String name;
    private int[] location;
    private int moves;
    private int value;

    public MovingItemDTOImpl(String name, int[]location, int moves, int value){
        this.name = name;
        this.location = location;
        this.moves = moves;
        this.value = value;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int[] getLocation() {
        return new int[0];
    }

    @Override
    public int getNumberOfMoves() {
        return 0;
    }

    @Override
    public int getValue() {
        return 0;
    }
}
