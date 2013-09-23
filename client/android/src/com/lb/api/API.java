package com.lb.api;

import android.location.Location;
import android.util.Log;

import com.lb.dao.AuthEntity;
import com.lb.model.Player;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class API {
	private static final String URL = "http://ackyla.com:3000/";
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return URL + relativeUrl;
	}

	public static void register(String name, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", name);
		post("users/create", params, handler);
	}

	public static void postLocation(AuthEntity auth, Location loc,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(auth.getUserId()));
		params.put("token", auth.getToken());
		params.put("latitude", Double.toString(loc.getLatitude()));
		params.put("longitude", Double.toString(loc.getLongitude()));
		post("locations/create", params, handler);
	}

	public static void getRoomLocations(int roomId,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("room_id", Integer.toString(roomId));
		get("locations/list", params, handler);
	}

	public static void createRoom(AuthEntity auth, String title, int timeLimit,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(auth.getUserId()));
		params.put("token", auth.getToken());
		params.put("title", title);
		params.put("time_limit", Integer.toString(timeLimit));
		post("rooms/create", params, handler);
	}

	public static void startGame(AuthEntity auth,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(auth.getUserId()));
		params.put("token", auth.getToken());
		post("users/start", params, handler);
	}

	public static void getRoomList(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		get("rooms/list", params, handler);
	}

	public static void enterRoom(AuthEntity auth, int roomId,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(auth.getUserId()));
		params.put("token", auth.getToken());
		params.put("room_id", Integer.toString(roomId));
		post("users/enter", params, handler);
	}

	public static void getRoomUsers(int roomId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("room_id", Integer.toString(roomId));
		get("rooms/users", params, handler);
	}

	public static void postHitLocation(AuthEntity auth, int targetId,
			double latitude, double longitude, double radius,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(auth.getUserId()));
		params.put("token", auth.getToken());
		params.put("target_user_id", Integer.toString(targetId));
		params.put("latitude", Double.toString(latitude));
		params.put("longitude", Double.toString(longitude));
		params.put("radius", Double.toString(radius));
		post("users/hit", params, handler);
	}

	public static void getRoomInfo(int roomId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("room_id", Integer.toString(roomId));
		get("rooms/show", params, handler);
	}

	public static void getUserInfo(int userId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(userId));
		get("users/show", params, handler);
	}

	public static void getTimeLeft(int roomId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("room_id", Integer.toString(roomId));
		post("rooms/timeleft", params, handler);
	}
}
