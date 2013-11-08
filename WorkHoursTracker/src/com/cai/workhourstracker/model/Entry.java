package com.cai.workhourstracker.model;

import java.math.BigDecimal;
import java.util.Date;

public class Entry {
	private int id;
	private String comment;
	private String startClock;
	private String stopClock;
	private int jobId;

	public BigDecimal getEarned_money() {
		return earned_money;
	}

	public void setEarned_money(BigDecimal earned_money) {
		this.earned_money = earned_money;
	}

	private BigDecimal earned_money;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStartClock() {
		return startClock;
	}

	public void setStartClock(String startClock) {
		this.startClock = startClock;
	}

	public String getStopClock() {
		return stopClock;
	}

	public void setStopClock(String stopClock) {
		this.stopClock = stopClock;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	private Job job;

	public Entry() {
		super();
	}

	public Entry(String comment, String startClock, String stopClock) {
		super();
		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
	}

	public Entry(String comment) {
		super();
		this.comment = comment;

	}

	public Entry(String comment, BigDecimal earned_money) {
		super();
		this.comment = comment;
		this.earned_money = earned_money;
	}

	public Entry(String comment, String startClock, String stopClock, int jobId,
			BigDecimal earned_money, Job job) {
		super();
		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
		this.jobId = jobId;
		this.earned_money = earned_money;
		this.job = job;
	}

	public Entry(int id, String comment, String startClock, String stopClock,
			Job job) {
		super();
		this.id = id;
		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
		this.job = job;
	}
}
