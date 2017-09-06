package com.delaru.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Command {
    public abstract CommandType getCommandType();
}
