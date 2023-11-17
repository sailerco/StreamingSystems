package org.example.CommandSide;

import org.example.CommandPrompts.*;
import org.example.EventAPI.EventStoreImpl;
import org.example.EventPrompts.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DomainModel {
    public static Map<String, Integer> IDsAndMoves = new HashMap<>();
    public static Map<String, int[]> usedPositions = new HashMap<>();
    EventStoreImpl eventStore = new EventStoreImpl();

    //The item will be added to the Maps and the Creation Event will be called.
    public void create(CommandCreateItem command) {
        if (!exists(command.id)) {
            IDsAndMoves.put(command.id, 0);
            usedPositions.put(command.id, command.location);
            storeEvent(new EventMovingItemCreated(command.id, command.location, command.value));
        } else System.out.println("Item with id " + command.id + " already exists");
    }

    //*The method moveItem checks if the Item exists and also if the new vector is != {0,0,0}
    // It sums up the old position with the vector and checks if the new position is already occupied by another item. If so, it deletes said Item.
    // The current items position is updated. We then check if the item was moved too often, if so it is also removed. Otherwise, it is stored in the EventStore.*//
    public void moveItem(CommandMoveItem command) {
        if (exists(command.id) && !Arrays.equals(command.vector, new int[]{0, 0, 0})) {
            int[] sum = sumArrays(usedPositions.get(command.id), command.vector);
            if (usedPositions.containsKey(getKey(sum))) remove(new CommandDeleteItem(getKey(sum)));

            usedPositions.put(command.id, sum);
            if (!movedOverLimit(command.id)) storeEvent(new EventMovingItemMoved(command.id, command.vector));
            else remove(command);

        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be moved");
    }

    //If the Item was moved >= 20 times it will be removed. Otherwise, the IDsAndMoves field is updated.
    public boolean movedOverLimit(String id) {
        int moves = IDsAndMoves.get(id) + 1;
        if (moves >= 20) return true;
        else {
            IDsAndMoves.put(id, moves);
            return false;
        }
    }

    public int[] sumArrays(int[] first, int[] second) {
        int[] sum = new int[3];
        for (int i = 0; i < first.length; i++)
            sum[i] = first[i] + second[i];
        return sum;
    }

    public String getKey(int[] position) {
        for (Map.Entry<String, int[]> entry : usedPositions.entrySet()) {
            if (Arrays.equals(entry.getValue(), position)) {
                return entry.getKey();
            }
        }
        return null;
    }

    //If the item exists the value will be changed
    public void changeValue(CommandChangeValue command) {
        if (exists(command.id)) storeEvent(new EventMovingItemChangedValue(command.id, command.newValue));
        else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be changed");
    }

    //If the item exists HashMaps will be updated and the Deletion Event will be called.
    public void remove(CommandPrompt command) {
        if (exists(command.id)) {
            IDsAndMoves.remove(command.id);
            usedPositions.remove(command.id);
            storeEvent(new EventMovingItemDeleted(command.id));
        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be deleted");
    }

    //checks if the item exists
    public Boolean exists(String id) {
        return IDsAndMoves.containsKey(id);
    }

    //Stores the Events in the EventStore.
    public void storeEvent(Event event) {
        eventStore.store(event);
    }
}
