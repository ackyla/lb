package api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class API {
	int userId;
	String token;
	
	public API(String name) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		try {
			JSONObject obj = post("users/create", params);
			this.userId = obj.getInt("id");
			this.token = obj.getString("token");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public API(int userId, String token) {
		this.userId = userId;
		this.token = token;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public JSONObject convertStreamToJson(InputStream is) throws JSONException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		return new JSONObject(sb.toString());
	}
	
	public JSONObject post(String path, Map<String, String> params) 
			throws MalformedURLException, ProtocolException, IOException, JSONException{
		URL url = new URL("http://ackyla.com:3000/" + path + ".json");
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(http.getOutputStream());
		int lastIndex = params.size();
		int i = 0;
		for(Map.Entry<String, String> e : params.entrySet()) {
			os.write(e.getKey() + "=" + e.getValue());
		    if (i != lastIndex) {
		    	os.write("&");
		    }
		    i++;
		    os.flush();
			os.close();
		}
		return convertStreamToJson(new BufferedInputStream(http.getInputStream()));
	}
	
	public JSONObject get(String path, Map<String, String> params) 
			throws MalformedURLException, ProtocolException, IOException, JSONException {
		URL url = new URL("http://localhost:3000/" + path);
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		return convertStreamToJson(new BufferedInputStream(http.getInputStream()));
	}
	
	public void postLocation(long time, double latitude, double longitude) {
		Log.v("api", "time="+time+", lat="+latitude+", lon="+longitude);
	}
}
