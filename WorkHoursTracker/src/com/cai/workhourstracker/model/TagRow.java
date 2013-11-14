package com.cai.workhourstracker.model;

public class TagRow {
	private String name;
	private boolean isChecked;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public TagRow(String name, boolean isChecked) {
		super();
		this.name = name;
		this.isChecked = isChecked;
	}
}
