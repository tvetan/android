package com.cai.workhourstracker.model;

import java.util.Comparator;

public class JobNameReverseComparator implements Comparator<Job> {

	@Override
	public int compare(Job left, Job right) {
		// TODO Auto-generated method stub
		return left.getName().compareTo(right.getName());
	}

}
