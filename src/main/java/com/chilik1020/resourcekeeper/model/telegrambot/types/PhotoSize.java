package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class PhotoSize {
	public String file_id;
	public int width, height;
	public Integer file_size;

	public PhotoSize(String file_id, int width, int height, Integer file_size) {
		this.file_id = file_id;
		this.width = width;
		this.height = height;
		this.file_size = file_size != null ? file_size : null;
	}
//
// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  strb.append("{\"file_id\": " + file_id + ", \"width\": " + width + ", \"height\": " + height);
//  if (file_size != null)
//   strb.append(", \"file_size\": " + file_size);
//  strb.append("}");
//
//  return strb.toString();
// }
}
