package com.lb.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.lb.R;
import com.lb.api.API;
import com.lb.model.Session;
import com.lb.model.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetAvatarActivity extends Activity {

	private static final int REQUEST_CODE = 100;
	private static final int AVATAR_WIDTH = 200;
	private static final int AVATAR_HEIGHT = 200;
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
			ImageSize imageSize = new ImageSize(AVATAR_WIDTH, AVATAR_HEIGHT);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.EXACTLY)
			.build();
			loader.loadImage(uri.toString(), imageSize, options, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
					API.updateAvatar(Session.getUser(), ThumbnailUtils.extractThumbnail(loadedImage, AVATAR_WIDTH, AVATAR_HEIGHT), new JsonHttpResponseHandler() {
						@Override
						public void onStart() {
							if (mProgressDialog == null) mProgressDialog = Utils.createProgressDialog(SetAvatarActivity.this);
							mProgressDialog.show();
						}
						
						@Override
						public void onSuccess(JSONObject json) {
							try {
								ImageLoader loader = ImageLoader.getInstance();
								DisplayImageOptions options = new DisplayImageOptions.Builder()
						        .cacheInMemory(true)
						        .cacheOnDisc(true)
						        .build();
								loader.displayImage(json.getString("avatar"), iv, options);
								Toast.makeText(SetAvatarActivity.this, "アバターを変更しました", Toast.LENGTH_LONG).show();
							} catch (JSONException e) {
								e.printStackTrace();
							}
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
