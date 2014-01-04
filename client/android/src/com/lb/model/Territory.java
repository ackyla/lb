package com.lb.model;

import net.vvakame.util.jsonpullparser.annotation.JsonKey;
import net.vvakame.util.jsonpullparser.annotation.JsonModel;

@JsonModel (decamelize=true)
public class Territory {
	
	@JsonKey
	private int id;
	@JsonKey
	private double latitude;
	@JsonKey
	private double longitude;
	@JsonKey
	private double radius;
	@JsonKey
	private double precision;
	@JsonKey
	private int detectionCount;
	@JsonKey
	private String expirationDate;
	@JsonKey
	private String createdAt;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLatitude() {
		return latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLongitude() {
		return longitude;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getRadius() {
		return radius;
	}
	
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getPrecision() {
		return precision;
	}
	
	public void setDetectionCount(int detectionCount) {
		this.detectionCount = detectionCount;
	}
	public int getDetectionCount() {
		return detectionCount;
	}
	
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getCreatedAt() {
		return createdAt;
	}
}
