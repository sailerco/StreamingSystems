package org.example.CommandSide;

import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {
    DomainModel model = new DomainModel();
    @BeforeEach
    public void setup(){
        DomainModel.idsAndMoves.clear();
        model.create(new CommandCreateItem("Tom", new int[]{0, 0, 0}, 0));
        model.create(new CommandCreateItem("Bob", new int[]{0, 0, 0}, 0));
        DomainModel.idsAndMoves.put("Tom", 19);
    }
    @Test
    public void createTest(){
        assertTrue(model.exists("Tom"));
        model.create(new CommandCreateItem("Tom", new int[]{0, 0, 0}, 0));
        //check if the key value was overwritten -> would be an error if it overwrites
        assertEquals(DomainModel.idsAndMoves.get("Tom"), 19);
    }
    @Test
    public void moveItemTest(){
        model.moveItem(new CommandMoveItem("Tom", new int[]{1, 1, 4}));
        assertFalse(model.exists("Tom"));

        model.moveItem(new CommandMoveItem("Bob", new int[]{3, 4, 1}));
        assertTrue(model.exists("Bob"));
        assertEquals(DomainModel.idsAndMoves.get("Bob"), 1);

        model.create(new CommandCreateItem("Alice", new int[]{0, 0, 0}, 0));
        model.moveItem(new CommandMoveItem("Alice", new int[]{3, 4, 1}));
        assertFalse(model.exists("Bob"));
    }

    @Test
    public void deleteItem(){
        model.remove(new CommandDeleteItem("Tom"));
        assertFalse(model.exists("Tom"));
    }
}