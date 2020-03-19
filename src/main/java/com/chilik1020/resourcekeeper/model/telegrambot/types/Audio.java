package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class Audio {
	public String file_id, mime_type;
	public int duration;
	public Integer file_size;

	public Audio(String file_id, int duration, String mime_type, Integer file_size) {
		this.file_id = file_id;
		this.duration = duration;
		this.mime_type = mime_type != null ? mime_type : null;
		this.file_size = file_size != null ? file_size : null;
	}
//
// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  strb.append("{\"file_id\": " + file_id + ", \"duration\": " + duration);
//  if (file_size != null)
//   strb.append(", \"file_size\": " + file_size);
//  if (mime_type != null)
//   strb.append(", \"mime_type\": " + mime_type);
//  strb.append("}");
//
//  return strb.toString();
// }
}
