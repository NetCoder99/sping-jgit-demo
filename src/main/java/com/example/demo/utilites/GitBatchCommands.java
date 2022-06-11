package com.example.demo.utilites;

import java.util.ArrayList;
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
					logger.info(tName);
					logger.info(tmpProps.getName());
					if (tmpProps.getName().equals(tName)) {
						tmpProps.setTracking(branch);
					}
				}
			}
		}
		return rtnList;
	}


}
