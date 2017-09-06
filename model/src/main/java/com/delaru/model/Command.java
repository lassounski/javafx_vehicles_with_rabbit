package com.delaru.model;


import java.io.Serializable;

public class Command implements Serializable {

    private CommandType commandType;

    public Command() {}

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
