package com.chilik1020.resourcekeeper.utils.data;

public class Command {
    private String name;
    private String path;

    public Command(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
