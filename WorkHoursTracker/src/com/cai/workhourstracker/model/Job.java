package com.cai.workhourstracker.model;

public class Job {
	private int id;
	private String name;
	private String created_at;

	// constructors
	public Job() {
	}

	public Job(String name) {
		this.name = name;
	}

	public Job(int id, String name) {
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

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
}
