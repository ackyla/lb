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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TerritoryListFragment extends ListFragment {
	
	private static final int INTERNAL_EMPTY_ID = 0x00ff0001;
	private static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
	private static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_territory_list, container, false);
    	
    	LinearLayout pframe = (LinearLayout) v.findViewById(R.id.progressContainer);
    	pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
    	
        FrameLayout lframe = (FrameLayout) v.findViewById(R.id.listContainer);
        lframe.setId(INTERNAL_LIST_CONTAINER_ID);
        
    	Button bt = (Button) v.findViewById(R.id.bt_create_territory);
    	bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SetTerritoryActivity.class);
				startActivity(intent);
			}
    		
    	});
        
	    return v;
	}
	
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

