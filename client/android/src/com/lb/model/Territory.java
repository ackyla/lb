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
}
