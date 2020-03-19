package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class Sticker {
	public String file_id;
	public int width, height;
	Integer file_size;
	public PhotoSize thumb;

	public Sticker(String file_id, int width, int height, PhotoSize thumb, Integer file_size) {
		this.file_id = file_id;
		this.width = width;
		this.height = height;
		this.thumb = thumb;
		this.file_size = file_size != null ? file_size : null;
	}
//
// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  strb.append("{\"file_id\": " + file_id + ", \"width\": " + width
//    + ", \"height\": " + height + ", \"thumb\": " + thumb);
//  if (file_size != null)
//   strb.append(", \"file_size\": " + file_size);
//  strb.append("}");
//
//  return strb.toString();
// }
}
