package com.lb.ui;

import java.util.List;

import com.lb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<NotificationData>{

	private LayoutInflater mLayoutInflater;
	
	public NotificationAdapter(Context context, int resourceId, List<NotificationData> objects) {
		super(context, resourceId, objects);
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationData item = (NotificationData) getItem(position);
		if (null == convertView) convertView = mLayoutInflater.inflate(R.layout.notification_list_item, null);
		
		TextView textView = (TextView)convertView.findViewById(R.id.notification_message_text);
		textView.setText(item.getMessage());
		
		return convertView;
	}
}