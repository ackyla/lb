package com.lb.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TerritoryListFragment extends ListFragment {
	private onTerritoryListItemClickListener listener;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		API.getUserTerritories(Session.getUser(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<TerritoryData> objects = new ArrayList<TerritoryData>();
				
				for(int i = 0; i < jsonArray.length(); i ++) {
					try {
						JSONObject json = jsonArray.getJSONObject(i);
						Integer id = json.getInt("id");
						Double latitude = json.getDouble("latitude");
						Double longitude = json.getDouble("longitude");
						TerritoryData item = new TerritoryData();
						item.setTextData("テリトリー_" + id);
						item.setId(id);
						item.setLatitude(latitude);
						item.setLongitude(longitude);
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TerritoryData item = (TerritoryData) l.getItemAtPosition(position);
		//listener.onTerritoryListItemClickListener(item.getLatitude(), item.getLongitude());
		
		TerritoryDetailFragment fragment = new TerritoryDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("id", item.getId());
		bundle.putDouble("latitude", item.getLatitude());
		bundle.putDouble("longitude", item.getLongitude());
		fragment.setArguments(bundle);
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();		
		fragmentTransaction.replace(R.id.fragment, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit(); 
	}
	
	public interface onTerritoryListItemClickListener {
		public void onTerritoryListItemClickListener(Double latitude, Double longitude);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//listener = (onTerritoryListItemClickListener) activity;
	}
}

