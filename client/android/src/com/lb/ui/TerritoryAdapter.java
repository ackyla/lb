package com.lb.ui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.lb.R;
import com.lb.model.Territory;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TerritoryAdapter extends ArrayAdapter<Territory>{

	private LayoutInflater mLayoutInflater;
	private Context mContext;
	
	public TerritoryAdapter(Context context, int resourceId, List<Territory> objects) {
		super(context, resourceId, objects);
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Territory item = (Territory)getItem(position);
		if (null == convertView) convertView = mLayoutInflater.inflate(R.layout.territory_list_item, null);
		
    	String addressString = new String();
    	Geocoder geocoder = new Geocoder(mContext, Locale.JAPAN);
    	if(geocoder.isPresent()) {
    		try {
    			List<Address> listAddress = geocoder.getFromLocation(item.getLatitude(), item.getLongitude(), 2);
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
		
		TextView tv1 = (TextView)convertView.findViewById(R.id.tv_name);
		tv1.setText("territory_" + item.getId());
		
		TextView tv2 = (TextView) convertView.findViewById(R.id.tv_radius);
		tv2.setText("半径: " + item.getRadius()/1000 + "km");
		
		TextView tv3 = (TextView) convertView.findViewById(R.id.tv_location);
    	tv3.setText("場所: " + addressString);
		
		return convertView;
	}
}