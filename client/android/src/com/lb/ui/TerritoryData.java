package com.lb.ui;

class TerritoryData {

	private String mTextData;
	private long mLatitude;
	private long mLongitude;
	
	public void setTextData(String text) {
		mTextData = text;
	}
	public String getTextData() {
		return mTextData;
	}
	
	public void setLatitude(long latitude) {
		mLatitude = latitude;
	}
	public long getLatitude() {
		return mLatitude;
	}
	
	public void setLongitude(long longitude) {
		mLongitude = longitude;
	}
	public long getLongitude() {
		return mLongitude;
	}
}
