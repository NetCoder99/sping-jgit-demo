package com.example.demo.models;

public class LocalRepoProps {

	private String name;
	private String tracking;
	private boolean needsPush;
	private boolean isCurrent;
	
	public LocalRepoProps() {
		super();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTracking() {
		return tracking;
	}
	public void setTracking(String tracking) {
		this.tracking = tracking;
	}
	public boolean isNeedsPush() {
		return needsPush;
	}
	public void setNeedsPush(boolean needsPush) {
		this.needsPush = needsPush;
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
}
