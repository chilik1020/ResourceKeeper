package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class Document {
	public String file_id, file_name, mime_type;
	public PhotoSize thumb;
	public Integer file_size;

	public Document(String file_id, PhotoSize thumb, String file_name, String mime_type, Integer file_size) {
		this.file_id = file_id;
		this.thumb = thumb != null ? thumb : null;
		this.file_name = file_name != null ? file_name : null;
		this.file_size = file_size != null ? file_size : null;
		this.mime_type = mime_type != null ? mime_type : null;
	}
//
// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  strb.append("{\"file_id\": " + file_id + ", \"thumb\": " + thumb);
//  if (file_name != null)
//   strb.append(", \"file_name\": " + file_name);
//  if (file_size != null)
//   strb.append(", \"file_size\": " + file_size);
//  if (mime_type != null)
//   strb.append(", \"mime_type\": " + mime_type);
//  strb.append("}");
//
//  return strb.toString();
// }
}
