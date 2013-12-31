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
	private Double radius;
	@JsonKey
	private Double precision;
	
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
	
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	public Double getRadius() {
		return radius;
	}
	
	public void setPrecision(Double precision) {
		this.precision = precision;
	}
	public Double getPrecision() {
		return precision;
	}
}
