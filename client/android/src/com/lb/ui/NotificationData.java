package com.lb.ui;

public class NotificationData {

	public static final int TYPE_DETECTED = 1;
	public static final int TYPE_DETECT = 2;
	
	private Integer mId;
	private Integer mType;
	private String mTitle;
	private String mMessage;

	public void setId(Integer id) {
		mId = id;
	}
	public Integer getId() {
		return mId;
	}
	
	public void setType(Integer type) {
		mType = type;
	}
	public Integer getType() {
		return mType;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	public String getTitle() {
		return mTitle;
	}
	
	public void setMessage(String message) {
		mMessage = message;
	}
	public String getMessage() {
		return mMessage;
	}
}
