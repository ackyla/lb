package com.lb.ui;

import com.lb.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameDropdownAdapter extends ArrayAdapter<GameDropdownData>{

	private LayoutInflater mLayoutInflater;
	
	public GameDropdownAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.org_item, parent, false);
        }else{
            view = convertView;
        }

        GameDropdownData item = getItem(position);
        TextView tv1 =  (TextView) view.findViewById(R.id.tv_title);
        tv1.setText(item.getTitle());
        TextView tv2 =  (TextView) view.findViewById(R.id.tv_message);
        tv2.setText(item.getMessage());
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb_gps_point);
        pb.setMax(item.getGpsPointMax());
        pb.setProgress(item.getGpsPoint());
        TextView tv3 =  (TextView) view.findViewById(R.id.tv_gps_point);
        tv3.setText("陣力 "+item.getGpsPoint()+"/"+item.getGpsPointMax());
        
        return view;
    }
 
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.org_item, parent, false);
        }else{
            view = convertView;
        }
        GameDropdownData item = getItem(position);
        ImageView icon = (ImageView)view.findViewById(R.id.iv_avatar);
        //icon.setImageResource(item.getIconId());
        //icon.setColorFilter(getContext().getColor(item.getColorId())));
        TextView tv1 = (TextView) view.findViewById(R.id.tv_title);
        tv1.setText(item.getTitle());
        TextView tv2 = (TextView) view.findViewById(R.id.tv_message);
        tv2.setText(item.getMessage());
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb_gps_point);
        pb.setMax(item.getGpsPointMax());
        pb.setProgress(item.getGpsPoint());
        TextView tv3 =  (TextView) view.findViewById(R.id.tv_gps_point);
        tv3.setText("陣力 "+item.getGpsPoint()+"/"+item.getGpsPointMax());

        return view;
    }
	
}
