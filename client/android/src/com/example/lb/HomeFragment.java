package com.example.lb;

import logic.user.UserLogic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import dao.room.RoomEntity;
import dao.user.UserEntity;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import api.API;

public class HomeFragment extends Fragment {
	
	private Marker hitMarker;
	private Circle hitCircle;
	private double hitRadius = 1000f;
	
	private UserEntity userEntity;
	private GoogleMap gMap;
	private SupportMapFragment mapFragment;
	
    public HomeFragment(){  
        setRetainInstance(true);  
    }  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("life", "home create");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_home, container, false);
    	
    	Log.v("life", "home createView");
    	TextView tv1 = (TextView)v.findViewById(R.id.textView1);
    	final TextView tv2 = (TextView)v.findViewById(R.id.textView2);
    	final LinearLayout userList = (LinearLayout)v.findViewById(R.id.userList);
    	
    	UserLogic userLogic = new UserLogic(getActivity());
    	userEntity = userLogic.getUser();
    	
    	if(userEntity != null){
    		tv1.setText(userEntity.getName());
    		API.getRoomInfo(userEntity.getRoomId(), new JsonHttpResponseHandler(){
    			@Override
    			public void onSuccess(JSONObject object) {
    				RoomEntity roomEntity = new RoomEntity(object);
    				tv2.setText("入室中の部屋: "+roomEntity.getTitle());
    				
    		    	API.getRoomUsers(roomEntity.getId(), new JsonHttpResponseHandler() {
    		    		
    		    		@Override
    		    		public void onSuccess(JSONArray jsonArray) {
    		    			for(int i = 0; i < jsonArray.length(); i++){
    		    				try {
    								JSONObject json = jsonArray.getJSONObject(i);
    								displayUser(userList, json);
    							} catch (JSONException e) {
    							}
    		    			}
    		    		}
    		    		
    		    		@Override
    		    		public void onFailure(Throwable e) {
    		    			Toast.makeText(getActivity(), "ユーザの取得に失敗しました！", Toast.LENGTH_SHORT).show();
    		    		}
    		    	});
    				
    			}
    		});
    		
    	}
    	
    	FragmentManager manager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		mapFragment = (SupportMapFragment)manager.findFragmentByTag("map");
		if(mapFragment == null){
			Log.v("home", "create map fragment");
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.mapLayout, mapFragment, "map");
			fragmentTransaction.commit();
		}else{
			Log.v("home", "map fragment already exists");
		}

		SeekBar seekBar = (SeekBar)v.findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				hitRadius = (double)progress;
				if(hitCircle != null){
					hitCircle.setRadius(hitRadius);
				}
			}
		});
		
		return v;
	}
	
	private void displayUser(LinearLayout userList, JSONObject json) {
		final UserEntity userEntity = new UserEntity(json);
		View v = getActivity().getLayoutInflater().inflate(R.layout.layout_user, null);
		TextView textView = (TextView)v.findViewById(R.id.textView1);
		textView.setText(userEntity.getUserId() + ": " + userEntity.getName());
		final LinearLayout locationList = (LinearLayout)v.findViewById(R.id.locationList);
		
    	API.getUserLocations(userEntity, null, new JsonHttpResponseHandler(){
    		@Override
    		public void onSuccess(JSONArray jsonArray) {
    			
    			PolylineOptions options = new PolylineOptions();
    			options.color(0xcc00ffff);
    			options.width(10);
    			options.geodesic(true); // 測地線で表示
    			
    			for(int i = 0; i < jsonArray.length(); i++){
    				try {
						JSONObject json = jsonArray.getJSONObject(i);
						displayLocation(userEntity, locationList, json);
						options.add(new LatLng(json.getDouble("latitude"), json.getDouble("longitude")));
					} catch (JSONException e) {
					}
    			}
    			
    			gMap.addPolyline(options);
    		}
    		
    		@Override
    		public void onFailure(Throwable e) {
    			Toast.makeText(getActivity(), "ロケーションの取得に失敗しました！", Toast.LENGTH_SHORT).show();
    		}
    	});
		
		userList.addView(v);
	}
	
	private void displayLocation(UserEntity userEntity, LinearLayout locationList, JSONObject json) {

		try {
			double lat = json.getDouble("latitude");
			double lon = json.getDouble("longitude");
			String time = json.getString("created_at");
			
			LatLng position = new LatLng(lat, lon);
			MarkerOptions options = new MarkerOptions();
			options.position(position);
			options.title(time);
			options.snippet(userEntity.getName());
			gMap.addMarker(options);
			
			CameraPosition sydney = new CameraPosition.Builder()
	        .target(position).zoom(21.0f)
	        .bearing(0).tilt(25).build();
			gMap.animateCamera(CameraUpdateFactory.newCameraPosition(sydney));

		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v("life", "home attach");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("life", "home activityCreated");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.v("life", "home Start");
		
		gMap = mapFragment.getMap();
		gMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng position) {
				if(hitMarker != null){
					hitMarker.remove();
				}
				if(hitCircle != null){
					hitCircle.remove();
				}
				
				MarkerOptions options = new MarkerOptions();
				options.position(position);
				options.title("touch!!!");
				options.draggable(true);
				options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
				hitMarker = gMap.addMarker(options);
				
				CircleOptions circleOptions = new CircleOptions();
				circleOptions.center(position);
				circleOptions.fillColor(Color.argb(20, 0, 255, 255));
				circleOptions.strokeColor(Color.argb(255, 0, 255, 255));
				circleOptions.strokeWidth(5f);
				circleOptions.radius(hitRadius);
				hitCircle = gMap.addCircle(circleOptions);
				Log.v("home", "user="+userEntity.getRoomId()+", "+userEntity.getUserId());
				API.postHitLocation(userEntity, userEntity.getUserId(), position.latitude, position.longitude, hitRadius, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject json) {
						Log.v("home", "json="+json.toString());
					}
				});
			}	
		});
		
		gMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker marker) {
				if(hitCircle != null){
					hitCircle.setCenter(marker.getPosition());
				}
			}
			
			@Override
			public void onMarkerDragEnd(Marker marker) {
				if(hitCircle != null){
					//hitCircle.setVisible(true);
					hitCircle.setRadius(hitRadius);
					hitCircle.setCenter(marker.getPosition());
				}
			}
			
			@Override
			public void onMarkerDrag(Marker marker) {
				if(hitCircle != null){
					hitCircle.setCenter(marker.getPosition());
				}
			}
		});
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.v("life", "home detach");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("life", "home destroy");
	}
}
