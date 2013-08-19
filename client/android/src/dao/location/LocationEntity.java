package dao.location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class LocationEntity {
	private int id;
	private int roomId;
	private int userId;
	private double longitude;
	private double latitude;
	private String createdAt;
	private String updatedAt;

	public LocationEntity() {
	}

	public LocationEntity(JSONObject json) {
		try {
			setId(json.getInt("id"));
		} catch (Exception e) {

		}
		try {
			setRoomId(json.getInt("room_id"));
		} catch (JSONException e) {

		}
		try {
			setUserId(json.getInt("user_id"));
		} catch (JSONException e) {

		}
		try {
			setLongitude(json.getDouble("longitude"));
		} catch (JSONException e) {

		}
		try {
			setLatitude(json.getDouble("latitude"));
		} catch (JSONException e) {

		}
		try {
			setCreatedAt(json.getString("created_at"));
		} catch (JSONException e) {

		}
		try {
			setUpdatedAt(json.getString("updated_at"));
		} catch (JSONException e) {

		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return this.createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt() {
		return this.updatedAt;
	}

	/**
	 * ISO8601日時を正規表現で分解して取得する
	 */
	public void getParsedCreatedAt(String ISO8601Time) {

		Pattern pattern = Pattern
				.compile("(\\d{4})-(\\d{2})-(\\d{2})T([0-9:]*)([.0-9]*)(.)(.*)");
		Matcher matcher = pattern.matcher(ISO8601Time);

		if (matcher.find()) {
			Log.v("time", "ALL:" + matcher.group(0));

			Log.v("time", "YY:" + matcher.group(1));
			Log.v("time", "MM:" + matcher.group(2));
			Log.v("time", "DD:" + matcher.group(3));

			Log.v("time", "TIME:" + matcher.group(4));
			Log.v("time", "msec:" + matcher.group(5));

			Log.v("time", "+-:" + matcher.group(6));
			Log.v("time", "TZ:" + matcher.group(7));

		}
	}
}
