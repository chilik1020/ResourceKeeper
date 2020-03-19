package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class ReplyKeyboardMarkup implements KeyboardFunction {
    String[][] keyboard;
    Boolean resize_keyboard, one_time_keyboard, selective;

    public ReplyKeyboardMarkup(String[][] keyboard, Boolean resize_keyboard, Boolean one_time_keyboard, Boolean selective) {
        this.keyboard = keyboard;
        if(resize_keyboard != null)
            this.resize_keyboard = resize_keyboard;
        if(one_time_keyboard != null)
            this.one_time_keyboard = one_time_keyboard;
        if(selective != null)
            this.selective = selective;
    }

    public String array2String(String[] st){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[");
        for (int i = 0; i < st.length-1; i++)
            strBuilder.append( "\"" + st[i] + "\", ");
        strBuilder.append("\"" + st[st.length-1] + "\"]");
        return strBuilder.toString();
    }

    public String arrayarray2String(String[][] st){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[");
        for (int i = 0; i < st.length-1; i++)
            strBuilder.append(array2String(st[i]) + ", ");
        strBuilder.append(array2String(st[st.length-1]) + "]");
        return strBuilder.toString();
    }

    @Override
    public String toString(){
        String key = "\"keyboard\" : " + arrayarray2String(this.keyboard);
        StringBuilder strb = new StringBuilder();
        strb.append("{");
        strb.append(key);
        if (selective != null)
            strb.append(", \"selective\": " + selective);
        if (resize_keyboard != null)
            strb.append(", \"resize_keyboard\": " + resize_keyboard);
        if (one_time_keyboard != null)
            strb.append(", \"one_time_keyboard\": " + one_time_keyboard);
        strb.append("}");

        return strb.toString();
    }
}
