package org.example.CommandSide;

import jakarta.jms.JMSException;
import org.example.Broker;
import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.EventAPI.EventHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.example.CommandSide.DomainModel.idsAndMoves;
import static org.example.CommandSide.DomainModel.usedPositions;
import static org.example.QuerySide.QueryModel.query_database;
import static org.junit.jupiter.api.Assertions.*;

class CommandImplTest {

    static Broker broker;
    static EventHandler eventHandler;
    CommandHandler commandHandler = CommandHandler.getInstance();

    CommandImplTest() throws Exception {
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
        query_database.clear();
        broker.stopBroker();
    }

    @BeforeEach
    void setup() throws JMSException {
        eventHandler = new EventHandler();
        query_database.clear();
        idsAndMoves.clear();
        usedPositions.clear();

        commandHandler.handle(new CommandCreateItem("Bob"));
        eventHandler.processMessage();
        commandHandler.handle(new CommandCreateItem("Tom", new int[]{1, 2, 3}, 0));
        eventHandler.processMessage();
    }

    @Test
    void createItem() {
        assertTrue(commandHandler.model.exists("Tom"));
        assertTrue(query_database.containsKey("Tom"));

        assertTrue(commandHandler.model.exists("Bob"));
        assertTrue(query_database.containsKey("Bob"));

        assertFalse(commandHandler.model.exists("Alice"));
        assertFalse(query_database.containsKey("Alice"));
    }

    @Test
    void moveItem() throws JMSException {
        commandHandler.handle(new CommandMoveItem("Bob", new int[]{0, 0, 2}));
        eventHandler.processMessage();

        assertArrayEquals(new int[]{0, 0, 2}, usedPositions.get("Bob"));
        assertEquals(1, idsAndMoves.get("Bob"));
        assertArrayEquals(new int[]{0, 0, 2}, query_database.get("Bob").getLocation());
        assertEquals(1, query_database.get("Bob").getNumberOfMoves());
    }

    @Test
    void moveCollision() throws JMSException {
        commandHandler.handle(new CommandMoveItem("Bob", new int[]{1, 2, 3}));
        eventHandler.processMessage();

        assertArrayEquals(new int[]{1, 2, 3}, query_database.get("Bob").getLocation());
        assertArrayEquals(new int[]{1, 2, 3}, usedPositions.get("Bob"));
        assertFalse(commandHandler.model.exists("Tom"));
        assertFalse(query_database.containsKey("Tom"));
    }

    @Test
    void changeValue() throws JMSException {
        commandHandler.handle(new CommandChangeValue("Bob", 5));
        eventHandler.processMessage();
        assertEquals(5, query_database.get("Bob").getValue());
    }

    @Test
    void deleteItem() throws JMSException {
        commandHandler.handle(new CommandDeleteItem("Bob"));
        eventHandler.processMessage();
        assertFalse(commandHandler.model.exists("Bob"));
        assertFalse(query_database.containsKey("Bob"));
    }
}