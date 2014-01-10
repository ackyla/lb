package com.lb.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetAvatarActivity extends Activity {

	private static final int REQUEST_CODE = 100;
	private ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_avatar);		
		
		Button bt = (Button) findViewById(R.id.bt_update);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	    		// ギャラリー起動
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, REQUEST_CODE);
			}
			
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			Uri uri = data.getData();
			final ImageView iv = (ImageView) findViewById(R.id.iv_avatar);
			ImageLoader loader = ImageLoader.getInstance();
			ImageSize imageSize = new ImageSize(100, 100);
			loader.loadImage(uri.toString(), imageSize, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
			    	
					API.updateAvatar(Session.getUser(), loadedImage, new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(SetAvatarActivity.this);
							mProgressDialog.show();
						}
						
						@Override
						public void onSuccess(JSONObject json) {
							iv.setImageBitmap(loadedImage);
						}

						@Override
						public void onFailure(Throwable throwable) {
							Toast.makeText(SetAvatarActivity.this, "アバターを変更できませんでした", Toast.LENGTH_LONG).show();
							Log.i("game","updateAvatarOnFailure="+ throwable);
						}
						
						@Override
						public void onFinish() {
							mProgressDialog.dismiss();
						}
					});
			    }
			});
		}
	}
}
