package com.lb.ui;

import com.lb.R;
import com.lb.logic.LocationUpdateService;
import com.lb.logic.ILocationUpdateServiceClient;
import com.lb.model.Session;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ConfigFragment extends Fragment implements ILocationUpdateServiceClient {
	
	private static Intent serviceIntent;
	
	private final ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v("main", "service connected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v("main", "service disconnected");
		}
		
	};
	
	public ConfigFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "config create");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final View v = inflater.inflate(R.layout.fragment_config, container, false);
		Log.v("life", "config createView");
		
		Button button1 = (Button)v.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startAndBindService();
			}
		});
		
		Button button2 = (Button)v.findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				stopAndUnBindService();
			}
		});
		
		TextView textView = (TextView)v.findViewById(R.id.textView1);
		textView.setText("gps start: "+Session.getIsStarted()+", gps bound: "+Session.getIsBound());
		
    	return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("life", "config attach");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("life", "config detach");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "config destroy");
	}

	private void startAndBindService() {
		Log.v("main", "start and bind");
		serviceIntent = new Intent(getActivity(), LocationUpdateService.class);
		getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
        Session.setIsBound(true);
	}
	
	private void stopAndUnBindService() {
		Log.v("main", "unbind");
        if (Session.getIsBound()) {
    	    getActivity().unbindService(conn);
            Session.setIsBound(false);
        }

        if(!Session.getIsStarted()) {
    		Log.v("main", "stop service");
        	getActivity().stopService(serviceIntent);	
        }
	}

	@Override
	public void onLocationUpdate(Location loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopLogging() {
		// TODO Auto-generated method stub
		
	}
}
