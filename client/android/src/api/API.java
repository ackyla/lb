package api;

import android.location.Location;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dao.user.UserEntity;

public class API {
	private static final String URL = "http://localhost:3000/";
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
	
	public static void postLocation(UserEntity user, Location loc, AsyncHttpResponseHandler handler) {
		// RequestParams params = null;
		loc.getTime();
		loc.getLatitude();
		loc.getLongitude();
		// post("", params, handler);
	}
	
	public static void createRoom(UserEntity user, String title, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		params.put("token", user.getToken());
		params.put("title", title);
		post("rooms/create", params, handler);
	}
	
	public static void getRoomList(UserEntity user, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		get("rooms/list", params, handler);
	}
}
