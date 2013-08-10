package api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class API {
	int userId;
	String token;
	
	public API(String name) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		try {
			InputStream stream = post("users/create", params);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// this.token = ret.token;
	}
	
	public API(int userId, String token) {
		this.userId = userId;
		this.token = token;
	}
	
	public InputStream post(String path, Map<String, String> params) 
			throws MalformedURLException, ProtocolException, IOException{
		URL url = new URL("http://localhost:3000/" + path);
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
		return new BufferedInputStream(http.getInputStream());
	}
	
	public InputStream get(String path, Map<String, String> params) 
			throws MalformedURLException, ProtocolException, IOException {
		URL url = new URL("http://localhost:3000/" + path);
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		BufferedInputStream stream = new BufferedInputStream(http.getInputStream());
		return stream;
	}
}
