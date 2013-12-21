package com.lb.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class TerritoryListFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		API.getUserTerritoryList(Session.getUser(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArray) {
				Log.i("game", "territory = "+ jsonArray.toString());

				List<TerritoryData> objects = new ArrayList<TerritoryData>();
				
				for(int i = 0; i < jsonArray.length(); i ++) {
					try {
						JSONObject json = jsonArray.getJSONObject(i);
						Integer id = json.getInt("id");
						TerritoryData item = new TerritoryData();
						item.setTextData("テリトリー_" + id);
						objects.add(item);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				TerritoryAdapter territoryAdapter = new TerritoryAdapter(getActivity(), 0, objects);
				setListAdapter(territoryAdapter);
			}

			@Override
			public void onFailure(Throwable throwable) {
				Log.i("game","getUserTerritoryListOnFailure="+ throwable);
			}
		});
	}
}

