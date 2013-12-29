package com.lb.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lb.api.API;
import com.lb.model.Session;
import com.loopj.android.http.JsonHttpResponseHandler;

public class NotificationListFragment extends ListFragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		API.getUserNotifications(Session.getUser(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<NotificationData> objects = new ArrayList<NotificationData>();
				
				for(int i = 0; i < jsonArray.length(); i ++) {
					try {
						JSONObject json = jsonArray.getJSONObject(i);
						NotificationData item = new NotificationData();
						item.setMessage("ほげ");
						objects.add(item);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				Context context = getActivity();
				if(context != null) {
					NotificationAdapter notificationAdapter = new NotificationAdapter(context, 0, objects);
					setListAdapter(notificationAdapter);
				}else{
					setListAdapter(null);
				}

			}

			@Override
			public void onFailure(Throwable throwable) {
				Log.i("game","getUserNotificationListOnFailure="+ throwable);
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		NotificationData item = (NotificationData) l.getItemAtPosition(position);
	}
}
