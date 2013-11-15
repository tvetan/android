package com.cai.workhourstracker.model;

import java.math.BigDecimal;
import java.util.Date;

import com.cai.workhourstracker.helper.Utils;

import android.util.Log;

public class Entry {
	private int id;
	private int jobId;
	private Integer payPeriodId;
	private String comment;
	private String startClock;
	private String stopClock;
	private Integer earnedMoney;
	private Integer baseRate;

	public Integer getPayPeriodId() {
		return payPeriodId;
	}

	public void setPayPeriodId(Integer payPeriodId) {
		this.payPeriodId = payPeriodId;
	}

	public Integer getEarnedMoney() {
		return earnedMoney;
	}

	public void setEarnedMoney(Integer earnedMoney) {
		this.earnedMoney = earnedMoney;
	}

	public Integer getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(Integer baseRate) {
		this.baseRate = baseRate;
	}

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

	public Integer getEarned_money() {
		return earnedMoney;
	}

	public void setEarned_money(Integer earned_money) {
		this.earnedMoney = earned_money;
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

	}

	public Entry(String comment, String startClock, String stopClock) {

		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
	}

	public Entry(String comment) {

		this.comment = comment;

	}

	public Entry(String comment, Integer earnedMoney) {

		this.comment = comment;
		this.earnedMoney = earnedMoney;
	}

	public Entry(String comment, String startClock, String stopClock,
			Integer jobId, Integer earnedMoney) {

		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
		this.jobId = jobId;
		this.earnedMoney = earnedMoney;
	}

	public Entry(String comment, String startClock, String stopClock,
			Integer jobId, Integer earnedMoney, Integer baseRate) {

		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
		this.jobId = jobId;
		this.earnedMoney = earnedMoney;
		this.baseRate = baseRate;

	}

	public Entry(String comment, String startClock, String stopClock,
			Integer jobId, Integer earnedMoney, Integer baseRate,
			Integer payPeriodId) {

		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
		this.jobId = jobId;
		this.earnedMoney = earnedMoney;
		this.baseRate = baseRate;
		this.payPeriodId = payPeriodId;

	}

	public Entry(String comment, String startClock, String stopClock,
			int jobId, Integer earnedMoney) {

		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;
		this.jobId = jobId;
		this.earnedMoney = earnedMoney;

	}

	public Entry(int id, String comment, String startClock, String stopClock) {

		this.id = id;
		this.comment = comment;
		this.startClock = startClock;
		this.stopClock = stopClock;

	}
}
