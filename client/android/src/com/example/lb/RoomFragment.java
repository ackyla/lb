package com.example.lb;

import logic.room.RoomLogic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class RoomFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_room, container, false);
    	
    	final RoomLogic roomLogic = new RoomLogic(getActivity());
    	
    	Button button = (Button)v.findViewById(R.id.buttonCreate);
    	button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				roomLogic.create("HOGE_ROOM");
			}
    		
    	});
    	
    	return v;
	}
}
