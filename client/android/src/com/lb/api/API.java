package com.lb.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.Application;
import android.graphics.Bitmap;
import android.location.Location;
import android.preference.PreferenceManager;

import com.lb.R;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.ui.PreferenceScreenActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class API {
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
		boolean debug = Session.getContext().getResources().getBoolean(R.bool.debug_mode);
		String url = Session.getContext().getResources().getString(R.string.server_url);
		if (debug) {
			url = PreferenceManager.getDefaultSharedPreferences(Session.getContext()).getString(PreferenceScreenActivity.PREF_KEY_DEBUG_MODE_URL, url);
		}
		return url + relativeUrl;
	}

	private static void setUserParams(User user, RequestParams params) {
		params.put("user_id", Integer.toString(user.getId()));
		params.put("token", user.getToken());
	}
	
	public static void register(String name, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", name);
		post("users/create", params, handler);
	}

	public static void postLocation(User user, Location loc,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("latitude", Double.toString(loc.getLatitude()));
		params.put("longitude", Double.toString(loc.getLongitude()));
		post("locations/create", params, handler);
	}

	public static void getUserInfo(int userId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(userId));
		get("users/show", params, handler);
	}

	public static void createTerritory(User user, double latitude, double longitude, int character_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("latitude", Double.toString(latitude));
		params.put("longitude", Double.toString(longitude));
		params.put("character_id", Integer.toString(character_id));
		post("territories/create", params, handler);
	}
	
	public static void destroyTerritory(User user, int territory_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("id", Integer.toString(territory_id));
		post("territories/destroy", params, handler);
	}
	
	public static void getUserTerritories(User user, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getId()));
		params.put("token", user.getToken());
		get("users/territories", params, handler);
	}
	
	public static void getTerritoryLocations(User user, int territory_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("id", Integer.toString(territory_id));
		params.put("user_id", Integer.toString(user.getId()));
		params.put("token", user.getToken());
		get("territories/locations", params, handler);
	}
	
	public static void getTerritoryDetections(User user, int territory_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("id", Integer.toString(territory_id));
		get("territories/detections", params, handler);
	}
	
	public static void getUserNotifications(User user, boolean all, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		if(all) params.put("all", "true");
		get("users/notifications", params, handler);
	}
	
	public static void readNotification(User user, int notification_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("notification_id", Integer.toString(notification_id));
		post("notifications/read", params, handler);
	}
	
	public static void getCharacterList(AsyncHttpResponseHandler handler) {
		get("characters/list", null, handler);
	}
	
	public static void getUserLocations(User user, String date, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("date", date);
		get("users/locations", params, handler);
	}
	
	public static void supplyGpsPoint(User user, int territoryId, int gpsPoint, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("id", Integer.toString(territoryId));
		params.put("gps_point", Integer.toString(gpsPoint));
		post("territories/supply", params, handler);
	}
	
	public static void updateAvatar(User user, Bitmap avatar, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		avatar.compress(Bitmap.CompressFormat.PNG, 85, out);
		byte[] byteArray = out.toByteArray();
		params.put("avatar", new ByteArrayInputStream(byteArray), "image.png");
		post("users/avatar", params, handler);
	}
}
