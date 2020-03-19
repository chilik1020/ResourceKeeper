package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class ForceReply implements KeyboardFunction {
    boolean force_reply = true;
    Boolean selective;

    public ForceReply(Boolean selective) {
        this.selective = selective;
    }

    @Override
    public String toString(){
        return selective != null ? "{\"force_reply\": true, \"selective\": " + selective.toString() + "}" : "{\"force_reply\": true}";
    }
}
