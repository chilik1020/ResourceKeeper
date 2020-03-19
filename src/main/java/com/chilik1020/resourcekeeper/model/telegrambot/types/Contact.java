package com.chilik1020.resourcekeeper.model.telegrambot.types;


public class Contact {
	public String phone_number, first_name, last_name;
	public Integer user_id;

	public Contact(String phone_number, String first_name, String last_name, Integer user_id) {
		this.phone_number = phone_number;
		this.first_name = first_name;
		this.last_name = last_name != null ? last_name : null;
		this.user_id = user_id != null ? user_id : null;
	}

// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  strb.append("{\"phone_number\": " + phone_number + ", \"first_name\": " + first_name);
//  if (last_name != null)
//   strb.append(", \"last_name\": " + last_name);
//  if (user_id != null)
//   strb.append(", \"user_id\": " + user_id);
//  strb.append("}");
//
//  return strb.toString();
// }
}
