package org.example.CommandSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.CommandPrompts.*;

import jakarta.jms.JMSException;

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

    public void handle(CommandCreateItem command) throws JMSException {
        model.create(command);
    }

    public void handle(CommandMoveItem command) throws JMSException {
        model.moveItem(command);
    }

    public void handle(CommandChangeValue command) throws JMSException {
        model.changeValue(command);
    }
    public void handle(CommandDeleteItem command) throws JMSException {
        model.remove(command);
    }
}
