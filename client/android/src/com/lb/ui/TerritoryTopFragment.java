package com.lb.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;

public class TerritoryTopFragment extends DialogFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_territory_top, container, false);
    
    	Button button1 = (Button) v.findViewById(R.id.territory_create_button);
    	button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
    		
    	});
    	
    	Button button2 = (Button) v.findViewById(R.id.territory_list_button);
    	button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();		
				fragmentTransaction.replace(R.id.fragment, new TerritoryListFragment());
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
    		
    	});

    	return v;
	}
}
