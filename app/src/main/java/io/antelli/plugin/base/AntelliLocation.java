package io.antelli.plugin.base;

import android.location.Location;

public class AntelliLocation extends Location {

	private String name;
	private String value;

	private AntelliLocation() {
		super("gps");
	}
	
	public AntelliLocation(String name, String value, double latitude, double longitude) {
		this();
		this.name = name;
		this.value = value;
		super.setLatitude(latitude);
		super.setLongitude(longitude);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name;
	}
}
