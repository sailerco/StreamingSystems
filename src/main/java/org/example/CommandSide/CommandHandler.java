package org.example.CommandSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.CommandPrompts.*;

import javax.jms.JMSException;

public class CommandHandler {
    DomainModel model = new DomainModel();

    // Singleton implementiert
    private static CommandHandler INSTANCE;

    public CommandHandler() throws Exception {
    }

    public static CommandHandler getInstance() throws Exception {
        if (INSTANCE == null)
            INSTANCE = new CommandHandler();
        return INSTANCE;
    }

    public void handle(CommandCreateItem command) throws JMSException, JsonProcessingException {
        model.create(command);
    }

    public void handle(CommandMoveItem command) throws JMSException, JsonProcessingException {
        model.moveItem(command);
    }

    public void handle(CommandChangeValue command) throws JMSException, JsonProcessingException {
        model.changeValue(command);
    }
    public void handle(CommandDeleteItem command) throws JMSException, JsonProcessingException {
        model.remove(command);
    }
}
