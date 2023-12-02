package org.example.CommandSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.QuerySide.QueryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.*;

class CommandImplTest {

    CommandHandler commandHandler = CommandHandler.getInstance();

    CommandImplTest() throws Exception {}

    @BeforeEach
    void setup(){
        QueryModel.query_database.clear();
        DomainModel.idsAndMoves.clear();
    }
    @Test
    void createItem() throws JMSException, JsonProcessingException {
        commandHandler.handle(new CommandCreateItem("Tom", new int[]{1, 2, 3}, 0));
        assertTrue(commandHandler.model.exists("Tom"));

        commandHandler.handle(new CommandCreateItem("Bob", new int[]{0, 0, 1}, 0));
        assertTrue(commandHandler.model.exists("Bob"));

        assertFalse(commandHandler.model.exists("Alice"));
    }

    @Test
    void moveItem() throws JMSException, JsonProcessingException {
        commandHandler.handle(new CommandCreateItem("Bob", new int[]{0,0,1}, 0));
        commandHandler.handle(new CommandMoveItem("Bob", new int[]{0, 0, 1}));
        assertArrayEquals(new int[]{0, 0, 2}, QueryModel.query_database.get("Bob").getLocation());
        assertEquals(1, QueryModel.query_database.get("Bob").getNumberOfMoves());
    }
    @Test
    void changeValue() throws JMSException, JsonProcessingException {
        commandHandler.handle(new CommandCreateItem("Bob", new int[]{0,0,1}, 0));
        commandHandler.handle(new CommandChangeValue("Bob", 5));
        assertEquals(5, QueryModel.query_database.get("Bob").getValue());
    }

    @Test
    void deleteItem() throws JMSException, JsonProcessingException {
        commandHandler.handle(new CommandCreateItem("Bob", new int[]{0,0,1}, 0));
        commandHandler.handle(new CommandDeleteItem("Bob"));
        assertFalse(commandHandler.model.exists("Bob"));
    }
}