package com.cai.workhourstracker.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayPeriod {

	private int id;
	private String date;
	private int jobId;
	private Integer money;

	public PayPeriod(String date, int jobId, Integer money) {

		this.date = date;
		this.jobId = jobId;
		this.money = money;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
}