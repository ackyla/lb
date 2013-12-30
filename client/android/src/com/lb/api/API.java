package com.lb.api;

import java.util.ArrayList;

import android.location.Location;

import com.lb.R;
import com.lb.model.Session;
import com.lb.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class API {
	private static final String URL = Session.getContext().getString(R.string.server_url);
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

	public static void createTerritory(User user, double latitude, double longitude, double radius, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("latitude", Double.toString(latitude));
		params.put("longitude", Double.toString(longitude));
		params.put("radius", Double.toString(radius));
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
	
	public static void getUserNotifications(User user, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getId()));
		params.put("token", user.getToken());
		get("users/notifications", params, handler);
	}
	
	public static void readNotification(User user, int notification_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		setUserParams(user, params);
		params.put("notification_id", Integer.toString(notification_id));
		post("notifications/read", params, handler);
	}
}
