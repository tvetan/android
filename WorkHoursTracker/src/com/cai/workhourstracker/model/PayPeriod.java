package com.cai.workhourstracker.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayPeriod {

	private int id;
	private Date date;
	private double workTime;
	private int JobId;
	private Job job;
	private BigDecimal money;

	public PayPeriod(int id, Date date, double workTime, int jobId, Job job,
			BigDecimal money) {
		super();
		this.id = id;
		this.date = date;
		this.workTime = workTime;
		JobId = jobId;
		this.job = job;
		this.money = money;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getWorkTime() {
		return workTime;
	}

	public void setWorkTime(double workTime) {
		this.workTime = workTime;
	}

	public int getJobId() {
		return JobId;
	}

	public void setJobId(int jobId) {
		JobId = jobId;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
