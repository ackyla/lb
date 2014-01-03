package com.lb.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.vvakame.util.jsonpullparser.JsonFormatException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.Territory;
import com.lb.model.TerritoryGen;
import com.lb.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TerritoryListFragment extends ListFragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		API.getUserTerritories(Session.getUser(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<Territory> objects = new ArrayList<Territory>();
				try {
					objects = TerritoryGen.getList(jsonArray.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JsonFormatException e1) {
					e1.printStackTrace();
				}
				
				Context context = getActivity();
				if(context != null) {
					TerritoryAdapter territoryAdapter = new TerritoryAdapter(context, 0, objects);
					setListAdapter(territoryAdapter);
				}else{
					setListAdapter(null);
				}

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
		Territory item = (Territory) l.getItemAtPosition(position);
		
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

}

