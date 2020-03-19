package com.chilik1020.resourcekeeper.model.telegrambot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.chilik1020.resourcekeeper.model.telegrambot.exceptions.TgApiException;
import com.chilik1020.resourcekeeper.model.telegrambot.types.*;
import com.chilik1020.resourcekeeper.utils.ConnectionUtilKt;
import com.chilik1020.resourcekeeper.utils.JsonConfig;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Bot {

	protected String token;
	public String API_URL_BASE = "https://api.telegram.org/bot";
	public String API_URL = "https://api.telegram.org/bot";

	private SimpleDateFormat sdfLog;

	public void setToken(String token) {
		this.token = token;
		this.API_URL = API_URL_BASE + token;
	}

	public Bot(String token) {
		this.token = token;
		this.API_URL = API_URL_BASE + token;
		this.sdfLog = new SimpleDateFormat("dd-MM-yy, HH:mm:ss");
		this.sdfLog.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
	}

	private CloseableHttpClient createAndInitializeHttpClient() {
		CloseableHttpClient httpclient;
		if (JsonConfig.proxyEnable) {
			HttpHost proxy = new HttpHost(JsonConfig.proxyAddress, JsonConfig.proxyPort, "http");
			DefaultProxyRoutePlanner planner = new DefaultProxyRoutePlanner(proxy);
			httpclient = HttpClients.custom().setRoutePlanner(planner).build();
		} else
			httpclient = HttpClients.createDefault();

		return httpclient;
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

	// Request
	private JSONObject request(String url, List<NameValuePair> data) throws UnsupportedOperationException, IOException, JSONException, TgApiException {
		return request(url, data, false);
	}

	private JSONObject request(String url, List<NameValuePair> data, Boolean returnAllJson) throws UnsupportedOperationException, IOException, JSONException, TgApiException {

		CloseableHttpClient httpclient = createAndInitializeHttpClient();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(data, StandardCharsets.UTF_8));

		CloseableHttpResponse postResponse = null;
		HttpEntity ent = null;

		while (ent == null) {
			if (!ConnectionUtilKt.isConnectionGood()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
				continue;
			}

			try {
				postResponse = httpclient.execute(httpPost);
				ent = postResponse.getEntity();
			}catch (Exception ex) {
				System.out.println("WARNING: " + ex.getMessage());
			}
		}


		JSONObject js;
		try {
			String s = IOUtils.toString(ent.getContent());
			js = new JSONObject(s);
		} finally {
			postResponse.close();
		}

		System.out.println("BOT RESPONSE: " + js);
		if(returnAllJson){
			if (js.has("ok")){
				if (!js.getBoolean("ok")){
					throw new TgApiException("Error " + js.getInt("error_code") + ": " + js.getString("description"));
				}
			}
			return js;
		} else {
			if (js.has("ok")){
				if (!js.getBoolean("ok")){
					throw new TgApiException("Error " + js.getInt("error_code") + ": " + js.getString("description"));
				} else {
					if (js.has("result"))
						return js.getJSONObject("result");
				}
			}
		}
		return js;
	}

	private JSONObject uploadFileRequest(String url, MultipartEntityBuilder builder, Boolean returnAllJson) throws UnsupportedOperationException, IOException, JSONException, TgApiException{
		CloseableHttpClient httpClient = createAndInitializeHttpClient();
		HttpPost uploadFile = new HttpPost(url);
		HttpEntity multipart = builder.build();

		uploadFile.setEntity(multipart);

		CloseableHttpResponse postResponse = null;
		HttpEntity responseEntity = null;

		while (responseEntity == null) {
			if (!ConnectionUtilKt.isConnectionGood()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
				continue;
			}

			try {
				postResponse = httpClient.execute(uploadFile);
				responseEntity = postResponse.getEntity();
			} catch (Exception ex) {
				System.out.println("WARNING: " + ex.getMessage());
			}
		}


		JSONObject js;
		try {
			js = new JSONObject(IOUtils.toString(responseEntity.getContent()));
		} finally {
			postResponse.close();
		}
		System.out.println("BOT RESPONSE: " + js);
		if(returnAllJson){
			if (js.has("ok")){
				if (!js.getBoolean("ok")){
					throw new TgApiException("Error " + js.getInt("error_code") + ": " + js.getString("description"));
				}
			}
			return js;
		} else {
			if (js.has("ok")){
				if (!js.getBoolean("ok")){
					throw new TgApiException("Error " + js.getInt("error_code") + ": " + js.getString("description"));
				} else {
					if (js.has("result"))
						return js.getJSONObject("result");
				}
			}
		}
		return js;
	}

	// Get Me
	public Peer getMe() throws ClientProtocolException, IOException, JSONException, UnsupportedOperationException, TgApiException {
		JSONObject jso = request(API_URL + "/getMe", new ArrayList<NameValuePair>());

		Peer p = null;
		if (jso.has("title")) {
			p = new Peer(jso.getInt("id"), jso.getString("title"), this);
		} else {
			switch (jso.length()) {
				case 2:
					p = new Peer(jso.getInt("id"), this, jso.getString("first_name"), null, null);
					break;
				case 3:
					if (jso.has("username"))
						p = new Peer(jso.getInt("id"), this, jso.getString("first_name"), null, jso.getString("username"));
					else
						p = new Peer(jso.getInt("id"), this, jso.getString("first_name"), jso.getString("last_name"), null);
					break;
				case 4:
					p = new Peer(jso.getInt("id"), this, jso.getString("first_name"), jso.getString("last_name"), jso.getString("username"));
					break;
			}
		}
		return p;
	}

	// Send Message
	public Message sendMessage(Integer chat_id, String text, Boolean disable_web_page_preview, Integer reply_to_message_id, KeyboardFunction reply_markup)
			throws ClientProtocolException, IOException, JSONException, UnsupportedOperationException, TgApiException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("text", text));

		if (disable_web_page_preview != null)
			data.add(new BasicNameValuePair("disable_web_page_preview", String.valueOf(disable_web_page_preview)));
		if (reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));
		return new Message(request(API_URL + "/sendMessage", data), this);
	}

	// Forward Message
	public Message forwardMessage(Integer chat_id, Integer from_chat_id, Integer message_id) throws ClientProtocolException, IOException, JSONException, UnsupportedOperationException, TgApiException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("from_chat_id", String.valueOf(from_chat_id)));
		data.add(new BasicNameValuePair("message_id", String.valueOf(message_id)));

		return new Message(request(API_URL + "/forwardMessage", data), this);
	}

	// Get Updates
	public Update[] getUpdates(Integer offset, Integer limit, Integer timeout) throws ClientProtocolException, IOException, JSONException, UnsupportedOperationException, TgApiException {
		String url = API_URL + "/getUpdates";

		List<NameValuePair> data = new ArrayList<NameValuePair>();
		if(offset != null)
			data.add(new BasicNameValuePair("offset", String.valueOf(offset)));
		if(limit != null)
			data.add(new BasicNameValuePair("limit", String.valueOf(limit)));
		if(timeout != null)
			data.add(new BasicNameValuePair("timeout", String.valueOf(timeout)));


		CloseableHttpResponse postResponse = null;
		HttpEntity ent = null;
		while (ent == null) {
			if (!ConnectionUtilKt.isConnectionGood()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
				continue;
			}

			try {
				CloseableHttpClient httpclient = createAndInitializeHttpClient();

				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(data, StandardCharsets.UTF_8));

				postResponse = httpclient.execute(httpPost);
				ent = postResponse.getEntity();
			} catch (Exception ex) {
				System.out.println("WARNING: " + ex.getMessage());
			}
		}

		JSONObject js;
		JSONArray jUpdates = null;
		try {
			String s = IOUtils.toString(ent.getContent());
			js = new JSONObject(s);
		} finally {
			postResponse.close();
		}

		if (js.has("ok")){
			if (!js.getBoolean("ok")){
				throw new TgApiException("Error " + js.getInt("error_code") + ": " + js.getString("description"));
			} else {
				if (js.has("result"))
					jUpdates = js.getJSONArray("result");
			}
		}

		Update[] updates = new Update[jUpdates.length()];

		if (updates.length > 0) {
			String time = sdfLog.format(new Date(System.currentTimeMillis()));
			System.out.println("BOT REQUEST: " + time + ", " + js);
		}

		for(int i = 0; i < jUpdates.length(); i++){
			JSONArray ja = jUpdates.getJSONObject(i).names();
			if (ja.get(1).toString().equals("edited_message"))
				updates[i] = new Update(jUpdates.getJSONObject(i).getInt("update_id"), new Message(jUpdates.getJSONObject(i).getJSONObject("edited_message"), this));
			else if (ja.get(1).toString().equals("channel_post"))
                updates[i] = new Update(jUpdates.getJSONObject(i).getInt("update_id"), new Message(jUpdates.getJSONObject(i).getJSONObject("channel_post"), this));
            else if (ja.get(1).toString().equals("edited_channel_post"))
                updates[i] = new Update(jUpdates.getJSONObject(i).getInt("update_id"), new Message(jUpdates.getJSONObject(i).getJSONObject("edited_channel_post"), this));
			else
				updates[i] = new Update(jUpdates.getJSONObject(i).getInt("update_id"), new Message(jUpdates.getJSONObject(i).getJSONObject("message"), this));

		}

		return updates;
	}

	// Set Webhook
	public String setWebhook(String url) throws UnsupportedOperationException, IOException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("url", url));

		return request(API_URL + "/setWebhook", data, true).getString("description");
	}

	// Send Photo
	public Message sendPhoto(Integer chat_id, File photo, String file_name, String caption, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("chat_id", String.valueOf(chat_id), ContentType.TEXT_PLAIN);
		builder.addBinaryBody("photo", photo, ContentType.APPLICATION_OCTET_STREAM, file_name);
		if(caption != null)
			builder.addTextBody("caption", caption, ContentType.TEXT_PLAIN);
		if(reply_to_message_id != null)
			builder.addTextBody("reply_to_message_id", String.valueOf(reply_to_message_id), ContentType.TEXT_PLAIN);
		if(reply_markup != null)
			builder.addTextBody("reply_markup", reply_markup.toString(), ContentType.TEXT_PLAIN);

		return new Message(uploadFileRequest(API_URL + "/sendPhoto", builder, false), this);
	}

	public Message sendPhoto(Integer chat_id, String file_id, String caption, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("photo", file_id));
		if(caption != null)
			data.add(new BasicNameValuePair("caption", caption));
		if(reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));

		return new Message(request(API_URL + "/sendPhoto", data), this);
	}

	// Send Audio
	public Message sendAudio(Integer chat_id, File photo, String file_name, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("chat_id", String.valueOf(chat_id), ContentType.TEXT_PLAIN);
		builder.addBinaryBody("audio", photo, ContentType.APPLICATION_OCTET_STREAM, file_name);
		if(reply_to_message_id != null)
			builder.addTextBody("reply_to_message_id", String.valueOf(reply_to_message_id), ContentType.TEXT_PLAIN);
		if(reply_markup != null)
			builder.addTextBody("reply_markup", reply_markup.toString(), ContentType.TEXT_PLAIN);

		return new Message(uploadFileRequest(API_URL + "/sendAudio", builder, false), this);
	}

	public Message sendAudio(Integer chat_id, String file_id, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("audio", file_id));
		if(reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));

		return new Message(request(API_URL + "/sendAudio", data), this);
	}

	// Send Document
	public Message sendDocument(Integer chat_id, File photo, String file_name, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("chat_id", String.valueOf(chat_id), ContentType.TEXT_PLAIN);
		builder.addBinaryBody("document", photo, ContentType.APPLICATION_OCTET_STREAM, file_name);
		if(reply_to_message_id != null)
			builder.addTextBody("reply_to_message_id", String.valueOf(reply_to_message_id), ContentType.TEXT_PLAIN);
		if(reply_markup != null)
			builder.addTextBody("reply_markup", reply_markup.toString(), ContentType.TEXT_PLAIN);

		return new Message(uploadFileRequest(API_URL + "/sendDocument", builder, false), this);
	}

	public Message sendDocument(Integer chat_id, String file_id, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("document", file_id));
		if(reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));

		return new Message(request(API_URL + "/sendDocument", data), this);
	}

	// Send Sticker
	public Message sendSticker(Integer chat_id, File photo, String file_name, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("chat_id", String.valueOf(chat_id), ContentType.TEXT_PLAIN);
		builder.addBinaryBody("sticker", photo, ContentType.APPLICATION_OCTET_STREAM, file_name);
		if(reply_to_message_id != null)
			builder.addTextBody("reply_to_message_id", String.valueOf(reply_to_message_id), ContentType.TEXT_PLAIN);
		if(reply_markup != null)
			builder.addTextBody("reply_markup", reply_markup.toString(), ContentType.TEXT_PLAIN);

		return new Message(uploadFileRequest(API_URL + "/sendSticker", builder, false), this);
	}

	public Message sendSticker(Integer chat_id, String file_id, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("sticker", file_id));
		if(reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));

		return new Message(request(API_URL + "/sendSticker", data), this);
	}

	// Send Video
	public Message sendVideo(Integer chat_id, File photo, String file_name, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("chat_id", String.valueOf(chat_id), ContentType.TEXT_PLAIN);
		builder.addBinaryBody("video", photo, ContentType.APPLICATION_OCTET_STREAM, file_name);
		if(reply_to_message_id != null)
			builder.addTextBody("reply_to_message_id", String.valueOf(reply_to_message_id), ContentType.TEXT_PLAIN);
		if(reply_markup != null)
			builder.addTextBody("reply_markup", reply_markup.toString(), ContentType.TEXT_PLAIN);

		return new Message(uploadFileRequest(API_URL + "/sendVideo", builder, false), this);
	}

	public Message sendVideo(Integer chat_id, String file_id, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("video", file_id));
		if(reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));

		return new Message(request(API_URL + "/sendVideo", data), this);
	}

	// Send Location
	public Message sendLocation(Integer chat_id, Long longitude, Long latitude, Integer reply_to_message_id, KeyboardFunction reply_markup) throws ClientProtocolException, IOException, UnsupportedOperationException, JSONException, TgApiException{
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
		data.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		if(reply_to_message_id != null)
			data.add(new BasicNameValuePair("reply_to_message_id", String.valueOf(reply_to_message_id)));
		if(reply_markup != null)
			data.add(new BasicNameValuePair("reply_markup", reply_markup.toString()));

		return new Message(request(API_URL + "/sendLocation", data), this);
	}

	// Send Chat Action
	public void sendChatAction(int chat_id, String action) throws UnsupportedOperationException, IOException, JSONException, TgApiException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("chat_id", String.valueOf(chat_id)));
		data.add(new BasicNameValuePair("action", action));

		request(API_URL + "/sendChatAction", data);
	}

	// Get User Profile Photos
	public UserProfilePhotos getUserProfilePhotos(int user_id, Integer offset, Integer limit) throws JSONException, UnsupportedOperationException, IOException, TgApiException{
		UserProfilePhotos userPhotos = null;
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		if(offset != null)
			data.add(new BasicNameValuePair("offset", String.valueOf(offset)));
		if(limit != null)
			data.add(new BasicNameValuePair("limit", String.valueOf(limit)));

		JSONObject json = request(API_URL + "/getUserProfilePhotos", data);

		ArrayList<PhotoSize[]> newPhotoSet = new ArrayList<>();
		for(int a = 0; a < json.getJSONArray("photos").length(); a++){
			ArrayList<PhotoSize> newPhoto = new ArrayList<>();
			for(int i = 0; i < json.getJSONArray("photo").getJSONObject(a).length(); i++){
				newPhoto.add(getPhotoSizeFromJson(json.getJSONArray("photo").getJSONArray(a).getJSONObject(i)));
			}
			if(!newPhoto.isEmpty()){
				newPhotoSet.add(newPhoto.toArray(new PhotoSize[newPhoto.size()]));
			}
		}
		if(!newPhotoSet.isEmpty()){
			userPhotos = new UserProfilePhotos(json.getInt("total_count"), newPhotoSet.toArray(new PhotoSize[newPhotoSet.size()][]));
		}
		return userPhotos;
	}
}
