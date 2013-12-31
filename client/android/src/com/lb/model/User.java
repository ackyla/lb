package com.lb.model;

import net.vvakame.util.jsonpullparser.annotation.JsonKey;
import net.vvakame.util.jsonpullparser.annotation.JsonModel;

@JsonModel (decamelize=true)
public class User {
	@JsonKey
	private static int id;
	@JsonKey
	private static String token;
	@JsonKey
	private static String name;
	@JsonKey
	private static int gps_point;
	@JsonKey
	private static int gps_point_limit;
	@JsonKey
	private static int level;
	@JsonKey
	private static int exp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getGps_Point() {
		return gps_point;
	}
	public void setGps_Point(int gps_point) {
		this.gps_point = gps_point;
	}
	
	public int getGps_Point_Limit() {
		return gps_point_limit;
	}
	public void setGps_Point_Limit(int gps_point_limit) {
		this.gps_point_limit = gps_point_limit;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
}
