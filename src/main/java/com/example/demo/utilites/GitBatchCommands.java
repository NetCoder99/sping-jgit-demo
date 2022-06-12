package com.example.demo.utilites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.models.LocalRepoProps;
import com.example.demo.models.ProcessResponse;

public class GitBatchCommands {
	static Logger logger = LoggerFactory.getLogger(GitBatchCommands.class);

	// -------------------------------------------------------------------------------------------------
	public static List<LocalRepoProps> GetBranches() throws Exception {
		String[] cmdArgs = new String[] {"git", "branch", "-a"};
		ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(cmdArgs);
		if (response1.getRetCd() != 0) {
			throw new Exception("Failed to fetch local branches");
		}
		
		List<LocalRepoProps> rtnList = new ArrayList<>();
		for (String branch : response1.getGoodResponse().split("\\R")) {
			if (!branch.contains("remotes")) {
				LocalRepoProps tmpProps = new LocalRepoProps();
				tmpProps.setName(branch.replaceAll("\\*", "").trim());
				tmpProps.setCurrent((branch.contains("*")));
				rtnList.add(tmpProps);
			}
			else {
				String tName = branch.split("/")[branch.split("/").length-1].trim();
				for(LocalRepoProps tmpProps : rtnList) {
					if (tmpProps.getName().equals(tName)) {
						tmpProps.setTracking(branch);
					}
				}
			}
		}
		return rtnList;
	}

	// -------------------------------------------------------------------------------------------------
	public static List<String> GetRemoteStatus() throws Exception {
		String[] cmdArgs = new String[] {"git", "remote", "show", "origin"};
		ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(cmdArgs);
		if (response1.getRetCd() != 0) {
			throw new Exception("Failed to execute git command: " + cmdArgs);
		}
		return Arrays.asList(response1.getGoodResponse().split("\\R"));
	}

	// -------------------------------------------------------------------------------------------------
	public static List<String> GetRemoteNames() throws Exception {
		String[] cmdArgs = new String[] {"git", "remote", "-v"};
		ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(cmdArgs);
		if (response1.getRetCd() != 0) {
			throw new Exception("Failed to execute git command: " + cmdArgs);
		}
		if (response1.getGoodResponse().trim().isEmpty()) {
			return new ArrayList<String>();
		}
		else {
			return Arrays.asList(response1.getGoodResponse().split("\\R"));
		}
	}

	// -------------------------------------------------------------------------------------------------
	public static List<String> AddRemote(String name, String htmlUrl) throws Exception {
		String[] cmdArgs = new String[] {"git", "remote", "add", name, htmlUrl};
		ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(cmdArgs);
		if (response1.getRetCd() != 0) {
			throw new Exception("Failed to execute git command: " + cmdArgs);
		}
		return GetRemoteNames();
		//return Arrays.asList(response1.getGoodResponse().split("\\R"));
	}

	// -------------------------------------------------------------------------------------------------
	public static List<String> RemoveRemote(String name) throws Exception {
		String[] cmdArgs = new String[] {"git", "remote", "rm", name};
		ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(cmdArgs);
		if (response1.getRetCd() != 0) {
			throw new Exception("Failed to execute git command: " + cmdArgs);
		}
		return GetRemoteNames();
		//return Arrays.asList(response1.getGoodResponse().split("\\R"));
	}

	// -------------------------------------------------------------------------------------------------
	public static List<LocalRepoProps> PushToRemote(String branch) throws Exception {
		String[] cmdArgs = new String[] {"git", "push", "-u", "origin", branch};
		ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(cmdArgs);
		if (response1.getRetCd() != 0) {
			throw new Exception("Failed to execute git command: " + cmdArgs);
		}
		
		List<LocalRepoProps> rtnList = new ArrayList<>();
		for (String respLine : response1.getGoodResponse().split("\\R")) {
			logger.info(respLine);
		}
		return rtnList;
	}
}
