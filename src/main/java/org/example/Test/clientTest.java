package org.example.Test;
import org.example.CommandSide.CommandImpl;
import org.example.ItemAggregate;
import org.example.MovingItem.MovingItemDTOImpl;
import org.junit.Test;

import java.util.Map;

import static org.example.QuerySide.QueryModel.query_database;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class clientTest {
    private CommandImpl command = new CommandImpl();
    private String name = "Tom";
    private int[] start = new int[]{2,3,4};
    private int value = 0;

    @Test
    public void testAddObject(){
        command.createItem(name, start, value);

        for (Map.Entry<String, MovingItemDTOImpl> entry : query_database.entrySet()) {
            assertEquals(name, entry.getValue().getName());
            assertEquals(start, entry.getValue().getLocation());
            assertEquals(value, entry.getValue().getValue());
            assertEquals(0, entry.getValue().getNumberOfMoves());
        }
    }
    @Test
    public void testMoveObject(){
        command.createItem(name, start, value);

        int[] move = new int[]{2,3,4};
        command.moveItem(name, move);

        for (Map.Entry<String, MovingItemDTOImpl> entry : query_database.entrySet()) {
            assertEquals(new int[]{4,6,8}, entry.getValue().getLocation());
            assertEquals(1, entry.getValue().getNumberOfMoves());
        }
    }

    /* todo:
    @Test
    public void testNoDuplicatesAllowed(){
        command.createItem(name, start, value);
        assertThrows(ItemAggregate.class, () -> {
            command.createItem(name, start, value);
        });
    }*/
}