package com.lb.ui;

public class GameDropdownData {

	private String mTitle;
	private String mMessage;
	private int mImage;
	private int gpsPoint;
	private int gpsPointMax;

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
	
	public void setImage(int image) {
		mImage = image;
	}
	public int getImage() {
		return mImage;
	}
	
	public void setGpsPoint(int gpsPoint) {
		this.gpsPoint = gpsPoint;
	}
	public int getGpsPoint() {
		return gpsPoint;
	}
	
	public void setGpsPointMax(int gpsPointMax) {
		this.gpsPointMax = gpsPointMax;
	}
	public int getGpsPointMax() {
		return gpsPointMax;
	}
}
