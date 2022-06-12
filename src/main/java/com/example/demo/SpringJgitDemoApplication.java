package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.models.GitHubRepoProps;
import com.example.demo.models.LocalRepoProps;
import com.example.demo.utilites.GitBatchCommands;
import com.example.demo.utilites.GitHubApiCommands;

@SpringBootApplication
public class SpringJgitDemoApplication {
	static Logger logger = LoggerFactory.getLogger(SpringJgitDemoApplication.class);
	
	enum OSType {
		  WINDOWS,
		  LINUX
		}
	OSType osType = null;

	private static String orgName   = "NetCoder99Org";
	private static String repoName  = "test1";
	
	public static void main(String[] args) {
		try {
			logger.info("+ SpingJgitDemoApplication started:{}");
			
			SpringApplication.run(SpringJgitDemoApplication.class, args);

			// ------------------------------------------------------------------
			// check for exit early 
			// ------------------------------------------------------------------
			
			// ------------------------------------------------------------------
			// check if remote exists, create if not found  
			// ------------------------------------------------------------------
			GitHubRepoProps gitHubRepo = CheckIfRemoteExists(orgName, repoName);
			
			// ------------------------------------------------------------------
			// check if local repo is tracking remote   
			// ------------------------------------------------------------------
			SetLocalToTrackRemote(gitHubRepo);
			
			// ------------------------------------------------------------------
			// finally push the updates to the remote   
			// ------------------------------------------------------------------
			PushBranches();
			
			logger.info("+ SpingJgitDemoApplication finished");
		}
		catch(Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

	
	// --------------------------------------------------------------------------------------------------
	// loop over the local branches and push the updates to GitHub 
	// --------------------------------------------------------------------------------------------------
	private static void PushBranches() throws Exception {
		List<String>          remoteStatus = GitBatchCommands.GetRemoteStatus();	
		List<LocalRepoProps>  branchesList = GitBatchCommands.GetBranches();
		for(LocalRepoProps branch : branchesList) {
			logger.info("++ Checking status of branch: {} ", branch.getName());
			if (CheckRemoteBranchStatus(branch.getName(), remoteStatus)) {
				logger.info("++ Pushing updates for: {}", branch.getName());
				GitBatchCommands.PushToRemote(branch.getName());
			}
		}
	}
	
	// --------------------------------------------------------------------------------------------------
	// check if remote exists, create it when not found on Github server 
	// --------------------------------------------------------------------------------------------------
	private static boolean CheckRemoteBranchStatus(String branchName, List<String> remoteStatus) throws Exception {
		// if the branch does not have a 'remote status' entry it needs to be pushed
		for (String remoteLine : remoteStatus) {
			if (remoteLine.contains(branchName) && remoteLine.contains("pushes to")) {
				logger.info("++ Branch found in remote status: {}", remoteLine);
				if (remoteLine.contains("fast-forwardable")) {
					return true;
				}

				if (remoteLine.contains("up to date")) {
					return false;
				}
				throw new Exception(String.format("Uknown remote status: %s", remoteLine));
			}
		}
		// fall through, new projects or branches will have no remote status and must be pushed 
		return true;
	}
	
	// --------------------------------------------------------------------------------------------------
	// check if remote exists, create if not found on Github server 
	// --------------------------------------------------------------------------------------------------
	private static GitHubRepoProps CheckIfRemoteExists(String orgName, String repoName) throws Exception {
		GitHubRepoProps gitHubRepo = GitHubApiCommands.getRemoteRepo(orgName, repoName);
		if (gitHubRepo == null) {
			gitHubRepo = GitHubApiCommands.createRemoteRepo(orgName, repoName);
		}
		return gitHubRepo;
	}
	
	// --------------------------------------------------------------------------------------------------
	// check if local repo is tracking remote   
	// --------------------------------------------------------------------------------------------------
	private static void SetLocalToTrackRemote(GitHubRepoProps gitHubRepo) throws Exception {
		List<String> remoteNames = GitBatchCommands.GetRemoteNames();
		if (remoteNames.size() == 0) {
			logger.info("++ Defining remote for: {}:{}", "origin", gitHubRepo.getHtmlUrl());
			remoteNames = GitBatchCommands.AddRemote("origin", gitHubRepo.getHtmlUrl());
		}
		
		List<String> remoteStatusList = GitBatchCommands.GetRemoteStatus();
		for(String tmp : remoteStatusList) {
			logger.info("++ {}", tmp);
		}
	}

}
