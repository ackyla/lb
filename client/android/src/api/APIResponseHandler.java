package api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class APIResponseHandler extends JsonHttpResponseHandler {
	protected Client client;

	APIResponseHandler() {

	}

	APIResponseHandler(Client client) {
		this.client = client;
	}

	protected Client getClient() {
		return client;
	}
}
