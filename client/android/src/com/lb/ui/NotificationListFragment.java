package com.lb.ui;

import java.text.SimpleDateFormat;
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
import com.lb.model.Utils;
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
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd' 'HH':'mm':'ss");
						Log.i("game", "json="+json);
						item.setId(json.getInt("notification_id"));
						
						String type = json.getString("notification_type");
						if(type.equals("entering")) {
							// みつかった
							item.setType(NotificationData.TYPE_DETECTED);
							item.setTitle(json.getJSONObject("territory_owner").getString("name") + " のテリトリーに入りました");
							item.setMessage(sdf.format(Utils.parseStringToDate(json.getString("created_at")))+" に見つかった");	
						}else{
							// みつけた
							item.setType(NotificationData.TYPE_DETECT);
							item.setTitle("テリトリー_"+json.getJSONObject("territory").getInt("territory_id")+" への侵入者発見");
							item.setMessage(sdf.format(Utils.parseStringToDate(json.getString("created_at")))+" に侵入");
						}
						
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
