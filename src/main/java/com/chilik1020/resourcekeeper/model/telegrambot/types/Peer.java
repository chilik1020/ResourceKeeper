package com.chilik1020.resourcekeeper.model.telegrambot.types;

import java.io.IOException;

import com.chilik1020.resourcekeeper.model.telegrambot.Bot;
import com.chilik1020.resourcekeeper.model.telegrambot.exceptions.TgApiException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

public class Peer {
	public int id;
	public Bot bot;
	public String first_name, last_name, username, title, type;

	public Peer(int id, Bot bot, String first_name, String last_name, String username) {
		this.bot = bot;
		this.type = "User";
		this.id = id;
		this.first_name = first_name;
		this.last_name  = last_name != null ? last_name : null;
		this.username   = username  != null ? username  : null;
	}

	public Peer(int id, String title, Bot bot) {
		this.bot = bot;
		this.type = "GroupChat";
		this.id = id;
		this.title = title;
	}

	public Message sendMsg(String text, Boolean disable_web_page_preview, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, UnsupportedOperationException, IOException, JSONException, TgApiException {
		return bot.sendMessage(this.id, text, disable_web_page_preview, reply_to_message_id, reply_markup);
	}
// @Override
// public String toString(){
//  StringBuilder strb = new StringBuilder();
//  if (this.type.equals("User")){
//   strb.append("{\"id\": " + id + ", \"first_name\": " + first_name);
//   if (last_name != null)
//    strb.append(", \"last_name\": " + last_name);
//   if (username != null)
//    strb.append(", \"username\": " + username);
//   strb.append("}");
//  } else {
//   strb.append("{\"id\": " + id + ", \"title\": " + title + "}");
//  }
//
//  return strb.toString();
// }
}
