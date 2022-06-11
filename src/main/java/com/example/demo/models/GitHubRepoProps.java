package com.example.demo;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class GitHubRepoProps {
	private static final AtomicInteger atomId = new AtomicInteger(0);
	private int id;
	private String repoId;
	private String repoName;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private LocalDateTime pushDate;
	
	@Override
	public String toString() {
		return String.valueOf(id) + ":" + repoId +":" + repoName;
	}

	public GitHubRepoProps() {
		super();
		this.id = atomId.incrementAndGet(); 
	}
	
	public GitHubRepoProps(String repoId, String repoName) {
		super();
		this.id = atomId.incrementAndGet(); 
		this.repoId = repoId;
		this.repoName = repoName;
	}

	public int getId() {
		return id;
	}

	public String getRepoId() {
		return repoId;
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public LocalDateTime getPushDate() {
		return pushDate;
	}

	public void setPushDate(LocalDateTime pushDate) {
		this.pushDate = pushDate;
	}

}
