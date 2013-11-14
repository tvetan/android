package com.cai.workhourstracker.model;

import java.util.Comparator;

public class JobNameComparator implements Comparator<Job> {

	@Override
	public int compare(Job left, Job right) {
		// TODO Auto-generated method stub
		return right.getName().compareTo(left.getName());
	}

}
