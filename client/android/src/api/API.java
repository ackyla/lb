package api;

import android.location.Location;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dao.user.UserEntity;

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

	public static void postLocation(UserEntity user, Location loc,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		params.put("token", user.getToken());
		params.put("latitude", Double.toString(loc.getLatitude()));
		params.put("longitude", Double.toString(loc.getLongitude()));
		post("locations/create", params, handler);
	}

	public static void getUserLocations(UserEntity user, Location loc,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		get("users/locations", params, handler);
	}

	public static void createRoom(UserEntity user, String title,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		params.put("token", user.getToken());
		params.put("title", title);
		post("rooms/create", params, handler);
	}

	public static void startGame(UserEntity user, String title,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		params.put("token", user.getToken());
		post("users/start", params, handler);
	}

	public static void getRoomList(UserEntity user,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		get("rooms/list", params, handler);
	}

	public static void enterRoom(UserEntity user, int roomId,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		params.put("token", user.getToken());
		params.put("room_id", Integer.toString(roomId));
		post("users/enter", params, handler);
	}

	public static void getRoomUsers(int roomId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("room_id", Integer.toString(roomId));
		get("rooms/users", params, handler);
	}

	public static void postHitLocation(UserEntity user, int targetId,
			double latitude, double longitude, double radius,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(user.getUserId()));
		params.put("token", user.getToken());
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
}
