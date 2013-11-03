package com.cai.workhourstracker.model;

public class Tag {
	private int id;

	private String name;

	public Tag() {

	}

	public Tag(String tag_name) {
		this.name = tag_name;
	}

	public Tag(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
