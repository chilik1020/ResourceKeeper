package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class ReplyKeyboardHide implements KeyboardFunction {
    boolean hide_keyboard = true;
    Boolean selective;

    public ReplyKeyboardHide(Boolean selective) {
        this.selective = selective != null ? selective : null;
    }

    @Override
    public String toString(){
        return selective != null ? "{\"hide_keyboard\": true, \"selective\": " + selective.toString() + "}" : "{\"hide_keyboard\": true}";
    }
}
