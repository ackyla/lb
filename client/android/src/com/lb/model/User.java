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
	private static int gpsPoint;
	@JsonKey
	private static int gpsPointLimit;
	@JsonKey
	private static int level;
	@JsonKey
	private static int exp;
	@JsonKey
	private static String avatar;
	
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
	
	public int getGpsPoint() {
		return gpsPoint;
	}
	public void setGpsPoint(int gpsPoint) {
		this.gpsPoint = gpsPoint;
	}
	
	public int getGpsPointLimit() {
		return gpsPointLimit;
	}
	public void setGpsPointLimit(int gpsPointLimit) {
		this.gpsPointLimit = gpsPointLimit;
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
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
