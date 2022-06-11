package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.models.GitHubRepoProps;
import com.example.demo.models.LocalRepoProps;
import com.example.demo.models.ProcessResponse;
import com.example.demo.utilites.EnvUtilities;
import com.example.demo.utilites.GitBatchCommands;
import com.example.demo.utilites.GitHubApiCommands;
import com.example.demo.utilites.ProcessBuilderNonBlocking;

@SpringBootApplication
public class SpringJgitDemoApplication {
	static Logger logger = LoggerFactory.getLogger(SpringJgitDemoApplication.class);
	
	enum OSType {
		  WINDOWS,
		  LINUX
		}
	OSType osType = null;
	
	public static void main(String[] args) {
		try {
			logger.info("SpingJgitDemoApplication started:{}");
			SpringApplication.run(SpringJgitDemoApplication.class, args);

			logger.info(EnvUtilities.GetOS().toString());
//			ProcessResponse response1 = ProcessBuilderNonBlocking.Execute(new String[] {"C:\\Users\\jdugger01\\bin\\areYouSure.bat"});
//			ProcessResponse response2 = ProcessBuilderNonBlocking.Execute(new String[] {"git", "remote", "show", "origin"});
//			ProcessResponse response3 = ProcessBuilderNonBlocking.Execute(new String[] {"git", "branch", "-a"});
//			//ProcessResponse response4 = ProcessBuilderNonBlocking.Execute(new String[] {"xfsdgit", "branch", "-a"});
//			ProcessResponse response5 = ProcessBuilderNonBlocking.Execute(new String[] {"git", "branch", "-ddda"});
//			//GitBatchCommands.GetYesNo();
			List<LocalRepoProps>  l1 = GitBatchCommands.GetBranches();
			List<GitHubRepoProps> r1 = GitHubApiCommands.getRepoNames(null);
//			logger.info("+++ {} repos found.", r1.size());
//			int delStatus = GitHubApiCommands.deleteRemoteRepo("test2");
//			logger.info("+++ {} delete status.", delStatus);
//			GitHubRepoProps newRepo = GitHubApiCommands.createRemoteRepo("test2");
//			logger.info("+++ {} was created.", newRepo.getRepoName());
			
			
			logger.info("SpingJgitDemoApplication finished");
		}
		catch(Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

}
