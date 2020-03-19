package com.chilik1020.resourcekeeper.model.telegrambot.types;


public class Location {
	public long longitude, latitude;

	public Location(long longitude, long latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

// @Override
// public String toString(){
//  return "{\"longitude\": " + longitude + ", \"latitude\": " + latitude + "}";
// }
}