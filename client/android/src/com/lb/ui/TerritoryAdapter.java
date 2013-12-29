package com.lb.ui;

import java.util.List;

import com.lb.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TerritoryAdapter extends ArrayAdapter<TerritoryData>{

	private LayoutInflater mLayoutInflater;
	
	public TerritoryAdapter(Context context, int resourceId, List<TerritoryData> objects) {
		super(context, resourceId, objects);
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TerritoryData item = (TerritoryData)getItem(position);
		if (null == convertView) convertView = mLayoutInflater.inflate(R.layout.territory_list_item, null);
		
		TextView textView = (TextView)convertView.findViewById(R.id.nname);
		textView.setText(item.getTextData());
		
		return convertView;
	}
}