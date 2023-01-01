package com.example.hotel.controller.factory;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.factory.impl.CommandsFactoryImpl;

public abstract class CommandsFactory {
    private static CommandsFactory commandsFactory;

    public abstract Command getCommand(final String uri);

    public static CommandsFactory getInstance() {
        if (commandsFactory == null) {
            synchronized (CommandsFactory.class) {
                if (commandsFactory == null) {
                    var temp = new CommandsFactoryImpl();
                    commandsFactory = temp;
                }
            }
        }
        return commandsFactory;
    }
}
