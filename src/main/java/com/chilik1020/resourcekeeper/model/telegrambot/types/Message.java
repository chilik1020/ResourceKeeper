package com.chilik1020.resourcekeeper.model.telegrambot.types;

import java.io.IOException;
import java.util.ArrayList;

import com.chilik1020.resourcekeeper.model.telegrambot.Bot;
import com.chilik1020.resourcekeeper.model.telegrambot.exceptions.TgApiException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;


public class Message {
	private Bot bot;

	// Required
	public int message_id;
	public Peer from;
	public int date;
	public Peer chat;

	// Optional
	public Peer forward_from;
	public int forward_date;
	public Message reply_to_message;
	public String text;
	public Audio audio;
	public Document document;
	public PhotoSize[] photo;
	public Sticker sticker;
	public Video video;
	public Voice voice;
	public String caption;
	public Contact contact;
	public Location location;
	public Peer new_chat_participant;
	public Peer left_chat_participant;
	public String new_chat_title;
	public PhotoSize[] new_chat_photo;
	public boolean delete_chat_photo;
	public boolean group_chat_created;

	protected Peer getPeerFromJson(JSONObject jso) throws JSONException{
		Peer p = null;
		if (jso.has("title")) {
			p = new Peer(jso.getInt("id"), jso.getString("title"), bot);
		} else {
			p = new Peer(jso.getInt("id"), bot, jso.getString("first_name"),
					jso.has("last_name") ? jso.getString("last_name") : null,
					jso.has("username") ? jso.getString("username") : null);
		}
		return p;
	}

	protected PhotoSize getPhotoSizeFromJson(JSONObject thumb) throws JSONException{
		PhotoSize ps = null;
		if (thumb.has("file_id")) {
			ps = new PhotoSize(thumb.getString("file_id"),
					thumb.getInt("width"),
					thumb.getInt("height"),
					thumb.has("height") ? thumb.getInt("height") : null);
		}
		return ps;
	}

	public Message(JSONObject json, Bot bot) throws JSONException, TgApiException {
		this.bot = bot;
		this.message_id = json.getInt("message_id");

		if (json.has("from"))
			this.from = getPeerFromJson(json.getJSONObject("from"));

		this.date = json.getInt("date");
		this.chat = getPeerFromJson(json.getJSONObject("chat"));

		if(json.has("forward_from")){
			this.forward_from = getPeerFromJson(json.getJSONObject("forward_from"));
			this.forward_date = json.getInt("forward_date");
		}

		if(json.has("reply_to_message"))
			this.reply_to_message = new Message(json.getJSONObject("reply_to_message"), bot);

		if(json.has("text"))
			this.text = json.getString("text");

		if(json.has("audio")){
			JSONObject aud = json.getJSONObject("audio");
			this.audio = new Audio(aud.getString("file_id"), aud.getInt("duration"),
					aud.has("mime_type") ? aud.getString("mime_type") : null,
					aud.has("file_size") ? aud.getInt("file_size") : null);
		}

		if(json.has("document")){
			JSONObject doc = json.getJSONObject("document");
			this.document = new Document(doc.getString("file_id"),
					doc.has("thumb") ? getPhotoSizeFromJson(doc.getJSONObject("thumb")) : null,
					doc.has("file_name") ? doc.getString("file_name") : null,
					doc.has("mime_type") ? doc.getString("mime_type") : null,
					doc.has("file_size") ? doc.getInt("file_size") : null);
		}

		if(json.has("photo")){
			ArrayList<PhotoSize> newPhoto = new ArrayList<>();
			for(int a = 0; a < json.getJSONArray("photo").length(); a++){
				newPhoto.add(getPhotoSizeFromJson(json.getJSONArray("photo").getJSONObject(a)));
			}
			if(!newPhoto.isEmpty()){
				this.photo = newPhoto.toArray(new PhotoSize[newPhoto.size()]);
			}
		}

		if(json.has("sticker")){
			JSONObject stick = json.getJSONObject("sticker");
			this.sticker = new Sticker(stick.getString("file_id"),
					stick.getInt("width"), stick.getInt("height"),
					stick.has("thumb") ? getPhotoSizeFromJson(stick.getJSONObject("thumb")) : null,
					stick.has("file_size") ? stick.getInt("file_size") : null);
		}

		if(json.has("video")){
			JSONObject vid = json.getJSONObject("video");
			this.video = new Video(vid.getString("file_id"), vid.getInt("width"),
					vid.getInt("height"), vid.getInt("duration"),
					vid.has("thumb") ? getPhotoSizeFromJson(vid.getJSONObject("thumb")) : null,
					vid.has("mime_type") ? vid.getString("mime_type") : null,
					vid.has("file_size") ? vid.getInt("file_size") : null);
		}

		if(json.has("voice")){
			JSONObject voi = json.getJSONObject("voice");
			this.voice = new Voice(voi.getString("file_id"), voi.getInt("duration"),
					voi.has("mime_type") ? voi.getString("mime_type") : null,
					voi.has("file_size") ? voi.getInt("file_size") : null);
		}

		this.caption = json.has("caption") ? json.getString("caption") : null;

		if(json.has("contact")){
			JSONObject con = json.getJSONObject("contact");
			this.contact = new Contact(con.getString("phone_number"), con.getString("first_name"),
					con.has("last_name") ? con.getString("last_name") : null,
					con.has("user_id") ? con.getInt("user_id") : null);
		}

		if(json.has("location"))
			this.location = new Location(json.getJSONObject("location").getLong("longitude"), json.getJSONObject("location").getLong("latitude"));

		if(json.has("new_chat_participant"))
			this.new_chat_participant = getPeerFromJson(json.getJSONObject("new_chat_participant"));

		if(json.has("left_chat_participant"))
			this.left_chat_participant = getPeerFromJson(json.getJSONObject("left_chat_participant"));

		if(json.has("new_chat_title"))
			this.new_chat_title = json.getString("new_chat_title");

		if(json.has("new_chat_photo")){
			ArrayList<PhotoSize> newPhoto = new ArrayList<>();
			for(int a = 0; a < json.getJSONArray("photo").length(); a++){
				newPhoto.add(getPhotoSizeFromJson(json.getJSONArray("photo").getJSONObject(a)));
			}
			if(!newPhoto.isEmpty()){
				this.new_chat_photo = newPhoto.toArray(new PhotoSize[newPhoto.size()]);
			}
		}

		if(json.has("delete_chat_photo"))
			this.delete_chat_photo = true;

		if(json.has("group_chat_created"))
			this.group_chat_created = true;
	}

	public Message replyTo(String text, Boolean disable_web_page_preview, KeyboardFunction reply_markup) throws ClientProtocolException, UnsupportedOperationException, IOException, JSONException, TgApiException{
		return bot.sendMessage(this.chat.id, text, disable_web_page_preview, this.message_id, reply_markup);
	}

	public Message forwardTo(int id) throws ClientProtocolException, UnsupportedOperationException, IOException, JSONException, TgApiException{
		return bot.forwardMessage(id, this.chat.id, this.message_id);
	}
//
// @Override
// public String toString(){
//  return "";
// }
}