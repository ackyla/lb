package com.lb.ui;

class TerritoryData {

	private String mTextData;
	private Integer mId;
	private Double mLatitude;
	private Double mLongitude;
	
	public void setTextData(String text) {
		mTextData = text;
	}
	public String getTextData() {
		return mTextData;
	}
	
	public void setId(Integer id) {
		mId = id;
	}
	public Integer getId() {
		return mId;
	}
	
	public void setLatitude(Double latitude) {
		mLatitude = latitude;
	}
	public Double getLatitude() {
		return mLatitude;
	}
	
	public void setLongitude(Double longitude) {
		mLongitude = longitude;
	}
	public Double getLongitude() {
		return mLongitude;
	}
}
