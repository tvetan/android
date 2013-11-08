package com.cai.workhourstracker.model;

public class Job {
	private int id;
	private String name;
	private String created_at;
	private Boolean isWorking;

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
	
	public Job(int id, String name, Boolean isWorking) {
		this.id = id;
		this.name = name;
		this.isWorking = isWorking;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
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
