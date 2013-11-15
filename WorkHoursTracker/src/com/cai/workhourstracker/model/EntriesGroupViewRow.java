package com.cai.workhourstracker.model;

import java.util.List;

import com.cai.workhourstracker.helper.Utils;

public class EntriesGroupViewRow {
	private String jobName;
	private Integer moneyEarned;
	private List<Entry> entries;
	private Integer workHours;

	public EntriesGroupViewRow(String jobName, Integer moneyEarned,
			List<Entry> entries, Integer workHours) {
		super();
		this.jobName = jobName;
		this.moneyEarned = moneyEarned;
		this.entries = entries;
		this.workHours = workHours;
	}

	public EntriesGroupViewRow(String jobName, List<Entry> entries) {
		this.jobName = jobName;
		this.entries = entries;
		this.workHours = getWorkHours(entries);
		this.moneyEarned = getMoneyEarned(entries);
	}
	
	private Integer getMoneyEarned(List<Entry> entries) {
		int sum = 0;
		for (Entry entry : entries) {
			sum += (Utils.differenceBetweenStartAndStop(entry) * entry.getBaseRate());
		}
		
		return sum;
	}
	

	private int getWorkHours(List<Entry> entries){
		int sum = 0;
		for (Entry entry : entries) {
			sum += Utils.differenceBetweenStartAndStop(entry);
		}
		
		return sum;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Integer getMoneyEarned() {
		return moneyEarned;
	}

	public void setMoneyEarned(Integer moneyEarned) {
		this.moneyEarned = moneyEarned;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Integer getWorkHours() {
		return workHours;
	}

	public void setWorkHours(Integer workHours) {
		this.workHours = workHours;
	}

}
