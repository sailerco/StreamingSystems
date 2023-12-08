package org.example.CommandSide;

import org.example.Broker;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.jms.JMSException;

import static org.example.CommandSide.DomainModel.idsAndMoves;
import static org.example.CommandSide.DomainModel.usedPositions;
import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {
    static Broker broker;
    DomainModel model = new DomainModel();

    DomainModelTest() throws Exception {
    }

    @BeforeAll
    static void start() throws Exception {
        broker = new Broker();
        broker.startBroker();
    }

    @AfterAll
    static void finish() throws Exception {
        usedPositions.clear();
        idsAndMoves.clear();
        broker.stopBroker();
    }

    @BeforeEach
    public void setup() throws JMSException {
        idsAndMoves.clear();
        model.create(new CommandCreateItem("Tom", new int[]{0, 0, 0}, 0));
        model.create(new CommandCreateItem("Bob", new int[]{0, 0, 0}, 0));
        idsAndMoves.put("Tom", 19);
    }

    @Test
    public void createTest() throws JMSException {
        assertTrue(model.exists("Tom"));
        model.create(new CommandCreateItem("Tom", new int[]{0, 0, 0}, 0));
        //check if the key value was overwritten -> would be an error if it overwrites
        assertEquals(idsAndMoves.get("Tom"), 19);
    }

    @Test
    public void moveItemTest() throws JMSException {
        model.moveItem(new CommandMoveItem("Tom", new int[]{1, 1, 4}));
        assertFalse(model.exists("Tom"));

        model.moveItem(new CommandMoveItem("Bob", new int[]{3, 4, 1}));
        assertTrue(model.exists("Bob"));
        assertEquals(idsAndMoves.get("Bob"), 1);

        model.create(new CommandCreateItem("Alice", new int[]{0, 0, 0}, 0));
        model.moveItem(new CommandMoveItem("Alice", new int[]{3, 4, 1}));
        assertFalse(model.exists("Bob"));
    }

    @Test
    public void deleteItem() throws JMSException {
        model.remove(new CommandDeleteItem("Tom"));
        assertFalse(model.exists("Tom"));
    }
}