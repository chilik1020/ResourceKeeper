package com.chilik1020.resourcekeeper.model.telegrambot.types;

public class Video {
	public String file_id, mime_type;
	public int width, height, duration;
	Integer file_size;
	public PhotoSize thumb;

	public Video(String file_id, int width, int height, int duration, PhotoSize thumb, String mime_type, Integer file_size) {
		this.file_id = file_id;
		this.width = width;
		this.height = height;
		this.duration = duration;
		this.thumb = thumb;
		this.file_size = file_size != null ? file_size : null;
		this.mime_type = mime_type != null ? mime_type : null;
	}
//
// @Override
// public String toString(){
//  String retString = "{\"file_id\": " + file_id + ", \"duration\": " + duration + ", \"width\": " + width
//    + ", \"height\": " + height + ", \"thumb\": " + thumb;
//  if (file_size != null)
//   retString += ", \"file_size\": " + file_size;
//  if (mime_type != null)
//   retString += ", \"mime_type\": " + mime_type;
//  if (caption != null)
//   retString +=  ", \"caption\": " + caption;
//
//  return retString + "}";
// }
}
