package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class Voice {
    public String file_id, mime_type;
    public int duration;
    public Integer file_size;

    public Voice(String file_id, int duration, String mime_type, Integer file_size) {
        this.mime_type = mime_type != null ? mime_type : null;
        this.file_size = file_size != null ? file_size : null;
    }

}
