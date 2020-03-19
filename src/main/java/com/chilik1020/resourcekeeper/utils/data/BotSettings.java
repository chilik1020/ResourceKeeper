package com.chilik1020.resourcekeeper.utils.data;

import java.util.List;

public class BotSettings {
    private String name;
    private String token;
    private List<Command> commands;

    public BotSettings(String name, String token, List<Command> command) {
        this.name = name;
        this.token = token;
        this.commands = command;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return "BotSettings{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", commands=" + commands.toString() +
                '}';
    }
}
