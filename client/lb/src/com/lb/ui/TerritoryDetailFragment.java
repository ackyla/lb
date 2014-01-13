package com.lb.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.User;
import com.lb.model.UserGen;
import com.lb.model.Utils;
import com.lb.ui.MapFragment.OnGoogleMapFragmentListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TerritoryDetailFragment extends Fragment {
	
	private static final int SUPPLY_GPS_POINT = 10;
	private ProgressDialog mProgressDialog;
	private OnTerritoryDetailFragmentListener listener;
	private Integer mId;
	private Double mLatitude;
	private Double mLongitude;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnTerritoryDetailFragmentListener) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_territory_detail, container, false);
    	
    	mId = getArguments().getInt("id");
    	mLatitude = getArguments().getDouble("latitude");
    	mLongitude = getArguments().getDouble("longitude");
    	
    	String addressString = new String();
    	Geocoder geocoder = new Geocoder(getActivity(), Locale.JAPAN);
    	if(geocoder.isPresent()) {
    		try {
    			List<Address> listAddress = geocoder.getFromLocation(mLatitude, mLongitude, 2);
    			if(listAddress.isEmpty()) {
    				addressString = "住所を特定できませんでした";
    			}else{
    				addressString = listAddress.get(1).getAddressLine(1) + "付近";
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}else{
    		addressString = "住所を特定できませんでした";
    	}
    	TextView nearbyText = (TextView) v.findViewById(R.id.territory_nearby_text);
    	nearbyText.setText(addressString);
    	
    	Button showButton = (Button) v.findViewById(R.id.territory_show_button);    	    	
    	showButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClickShowTerritoryButton(mLatitude, mLongitude);
			}
    		
    	});
    	
    	Button destroyButton = (Button) v.findViewById(R.id.territory_destroy_button);
    	destroyButton.setOnClickListener(new OnClickListener() {    		
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("削除するの？");
					builder.setPositiveButton("する", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							API.destroyTerritory(Session.getUser(), mId, new JsonHttpResponseHandler() {
								@Override
								public void onStart() {
									if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(getActivity());
									mProgressDialog.show();
								}
								
								@Override
								public void onSuccess(JSONObject json) {
									getFragmentManager().popBackStack();
									Toast.makeText(getActivity(), "テリトリーを削除しました", Toast.LENGTH_LONG).show();
								}

								@Override
								public void onFailure(Throwable throwable) {
									Log.i("game","getUserTerritoryListOnFailure="+ throwable);
								}
								
								@Override
								public void onFinish() {
									mProgressDialog.dismiss();
								}
							});
						}
					});

					builder.setNegativeButton("しない", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					builder.create().show();
			}
    		
    	});
    	
    	Button supplyButton = (Button) v.findViewById(R.id.bt_supply);
    	supplyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("テリトリーを回復します");
				builder.setMessage("消費陣力: " + SUPPLY_GPS_POINT);
				builder.setPositiveButton("回復", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						API.supplyGpsPoint(Session.getUser(), mId, SUPPLY_GPS_POINT, new JsonHttpResponseHandler() {
							@Override
							public void onStart() {
								if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(getActivity());
								mProgressDialog.show();
							}
							
							@Override
							public void onSuccess(JSONObject json) {
								// TODO
								User user = Session.getUser();
								user.setGpsPoint(user.getGpsPoint() - SUPPLY_GPS_POINT);
								Utils.updateSessionUserInfo(user);
								listener.onSupply();
								Toast.makeText(getActivity(), "回復しました", Toast.LENGTH_LONG).show();
							}

							@Override
							public void onFailure(Throwable throwable) {
								Log.i("game","getUserTerritoryListOnFailure="+ throwable);
								Toast.makeText(getActivity(), "回復できませんでした", Toast.LENGTH_LONG).show();
							}
							
							@Override
							public void onFinish() {
								mProgressDialog.dismiss();
							}
						});
					}
				});

				builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				
				builder.create().show();
			}	
    	});
    	
    	return v;
	}
	
    public static interface OnTerritoryDetailFragmentListener {
        void onClickShowTerritoryButton(Double latitude, Double longitude);
        void onSupply();
    }
}
