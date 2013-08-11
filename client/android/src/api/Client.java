package api;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

public class Client {
	private int userId;
	private String token;
	
	public Client(String name) {
		RequestParams params = new RequestParams();
		params.put("name", name);
		API.post("user/create", params, new APIResponseHandler(this) {
			@Override
			public void onSuccess(JSONObject json) {
				try {
					getClient().setUserId(json.getInt("name"));
					getClient().setToken(json.getString("token"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public Client(int userId, String token) {
		this.userId = userId;
		this.token = token;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
