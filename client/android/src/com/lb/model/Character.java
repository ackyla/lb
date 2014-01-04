package com.lb.model;

import net.vvakame.util.jsonpullparser.annotation.JsonKey;
import net.vvakame.util.jsonpullparser.annotation.JsonModel;

@JsonModel (decamelize=true)
public class Character {
	
	@JsonKey
	private int id;
	@JsonKey
	private String name;
	@JsonKey
	private double radius;
	@JsonKey
	private double precision;
	@JsonKey
	private int cost;
	@JsonKey
	private double distance;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
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
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getCost() {
		return cost;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDistance() {
		return distance;
	}
}
