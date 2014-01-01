package com.lb.ui;

import java.util.List;

import com.lb.R;
import com.lb.model.Character;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CharacterAdapter extends ArrayAdapter<Character>{

	private LayoutInflater mLayoutInflater;
	
	public CharacterAdapter(Context context, List<Character> objects) {
		super(context, 0, objects);
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Character item = (Character) getItem(position);
		if (null == convertView) convertView = mLayoutInflater.inflate(R.layout.character_list_item, null);
		
		TextView tv1 = (TextView)convertView.findViewById(R.id.tv_name);
		tv1.setText(item.getName());
		
		TextView tv2 = (TextView)convertView.findViewById(R.id.tv_radius);
		tv2.setText("半径: "+item.getRadius()/1000+"km");
		
		TextView tv3 = (TextView)convertView.findViewById(R.id.tv_precision);
		tv3.setText("精度: "+item.getPrecision()*100+"%");
		
		TextView tv4 = (TextView)convertView.findViewById(R.id.tv_cost);
		tv4.setText("消費陣力: "+item.getCost());

		return convertView;
	}
}
