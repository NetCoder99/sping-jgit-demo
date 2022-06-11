package com.example.demo.models;

import java.time.LocalDateTime;

public class ProcessResponse {
	private String[] cmdArgs;
	private int retCd;
	private String goodResponse;
	private String errResponse;
	private LocalDateTime execStartTime;
	private LocalDateTime execEndTime;

	
	
	public ProcessResponse() {
		super();
	}

	public ProcessResponse(String[] cmdArgs, int retCd, String goodResponse, String errResponse,
			LocalDateTime execStartTime, LocalDateTime execEndTime) {
		super();
		this.cmdArgs = cmdArgs;
		this.retCd = retCd;
		this.goodResponse = goodResponse;
		this.errResponse = errResponse;
		this.execStartTime = execStartTime;
		this.execEndTime = execEndTime;
	}

	public String[] getCmdArgs() {
		return cmdArgs;
	}

	public void setCmdArgs(String[] cmdArgs) {
		this.cmdArgs = cmdArgs;
	}

	public int getRetCd() {
		return retCd;
	}

	public void setRetCd(int retCd) {
		this.retCd = retCd;
	}

	public String getGoodResponse() {
		return goodResponse;
	}

	public void setGoodResponse(String goodResponse) {
		this.goodResponse = goodResponse;
	}

	public String getErrResponse() {
		return errResponse;
	}

	public void setErrResponse(String errResponse) {
		this.errResponse = errResponse;
	}

	public LocalDateTime getExecStartTime() {
		return execStartTime;
	}

	public void setExecStartTime(LocalDateTime execStartTime) {
		this.execStartTime = execStartTime;
	}

	public LocalDateTime getExecEndTime() {
		return execEndTime;
	}

	public void setExecEndTime(LocalDateTime execEndTime) {
		this.execEndTime = execEndTime;
	}

	
}
