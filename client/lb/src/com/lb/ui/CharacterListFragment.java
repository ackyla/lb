package com.lb.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import net.vvakame.util.jsonpullparser.JsonFormatException;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lb.api.API;
import com.lb.model.CharacterGen;
import com.lb.model.Session;
import com.lb.model.Utils;
import com.lb.model.Character;
import com.lb.ui.TerritoryDetailFragment.OnTerritoryDetailFragmentListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class CharacterListFragment extends ListFragment {
	
	private OnCharacterListFragmentItemClickListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnCharacterListFragmentItemClickListener) activity;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		API.getCharacterList(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<Character> objects = new ArrayList<Character>();
				try {
					objects = CharacterGen.getList(jsonArray.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JsonFormatException e) {
					e.printStackTrace();
				}

				Context context = getActivity();
				if(context != null) {
					CharacterAdapter adapter = new CharacterAdapter(context, objects);
					setListAdapter(adapter);
				}else{
					setListAdapter(null);
				}

			}

			@Override
			public void onFailure(Throwable throwable) {
				Log.i("game","getCharacterListOnFailure="+ throwable);
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Character item = (Character) l.getItemAtPosition(position);
		listener.onClickCharacterListItem(item);
	}
	
    public static interface OnCharacterListFragmentItemClickListener {
        void onClickCharacterListItem(Character character);
    }
}
