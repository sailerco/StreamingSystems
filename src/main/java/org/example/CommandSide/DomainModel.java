package org.example.CommandSide;

import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.CommandPrompts.CommandPrompt;
import org.example.EventAPI.EventStoreImpl;
import org.example.EventPrompts.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DomainModel {
    public static final int MOVES_LIMIT = 20;
    public static Map<String, Integer> idsAndMoves = new HashMap<>();
    public static Map<String, int[]> usedPositions = new HashMap<>();
    EventStoreImpl eventStore = new EventStoreImpl();

    //The item will be added to the Maps and the Creation Event will be called.
    public void create(CommandCreateItem command) {
        if (!exists(command.id)) {
            idsAndMoves.put(command.id, 0);
            usedPositions.put(command.id, command.location);
            storeEvent(new EventMovingItemCreated(command.id, command.location, command.value));
        } else System.out.println("Item with id " + command.id + " already exists");
    }

    //If the item exists the value will be changed
    public void changeValue(CommandChangeValue command) {
        if (exists(command.id)) storeEvent(new EventMovingItemChangedValue(command.id, command.newValue));
        else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be changed");
    }

    //If the item exists HashMaps will be updated and the Deletion Event will be called.
    public void remove(CommandPrompt command) {
        if (exists(command.id)) {
            removeFromHashes(command.id);
            storeEvent(new EventMovingItemDeleted(command.id));
        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be deleted");
    }

    //*The method moveItem checks if the Item exists and also if the new vector is != {0,0,0}.
    // We then check if the item would have been moved too often, if so it is also removed.
    // It sums up the old position with the vector and checks if the new position is already occupied by another item. If so, it deletes said Item.
    // The current items position is updated and then stored in the EventStore.*//
    public void moveItem(CommandMoveItem command) {
        if (exists(command.id) && !Arrays.equals(command.vector, new int[]{0, 0, 0})) {
            if (!movedOverLimit(command.id)) {
                int[] newPosition = computePosition(usedPositions.get(command.id), command.vector);
                String key = getKeyByPosition(newPosition);
                if (usedPositions.containsKey(key)) handleCollisionAndMove(key, command.id, command.vector);
                else storeEvent(new EventMovingItemMoved(command.id, command.vector));
            } else remove(command);
        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be moved");
    }

    //the following methods are helper functions
    private void handleCollisionAndMove(String collidedItem, String movedItem, int[] position) {
        storeEvent(new EventDeleteItemAndMoveAnotherItem(collidedItem, movedItem, position));
        usedPositions.put(movedItem, position);
        removeFromHashes(collidedItem);
    }

    //If the Item was moved >= 20 times it will be removed. Otherwise, the IDsAndMoves field is updated.
    public boolean movedOverLimit(String id) {
        int moves = idsAndMoves.get(id) + 1;
        if (moves >= MOVES_LIMIT) return true;
        else {
            idsAndMoves.put(id, moves);
            return false;
        }
    }

    private int[] computePosition(int[] first, int[] second) {
        int[] sum = new int[3];
        for (int i = 0; i < first.length; i++)
            sum[i] = first[i] + second[i];
        return sum;
    }

    public String getKeyByPosition(int[] position) {
        for (Map.Entry<String, int[]> entry : usedPositions.entrySet()) {
            if (Arrays.equals(entry.getValue(), position)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void removeFromHashes(String item) {
        idsAndMoves.remove(item);
        usedPositions.remove(item);
    }

    public Boolean exists(String id) {
        return idsAndMoves.containsKey(id);
    }

    public void storeEvent(Event event) {
        eventStore.store(event);
    }
}
