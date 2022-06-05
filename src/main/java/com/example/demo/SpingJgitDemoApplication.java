package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpingJgitDemoApplication {
	static Logger logger = LoggerFactory.getLogger(SpingJgitDemoApplication.class);
	
	
	public static void main(String[] args) {
		try {
			logger.info("SpingJgitDemoApplication started:{}");
			SpringApplication.run(SpingJgitDemoApplication.class, args);

			List<GitHubRepoProps> r1 = JGitCommands.getRepoNames(null);
			logger.info("+++ {} repos found.", r1.size());
			
			int delStatus = JGitCommands.deleteRemoteRepo("test2");
			logger.info("+++ {} delete status.", delStatus);

			GitHubRepoProps newRepo = JGitCommands.createRemoteRepo("test2");
			logger.info("+++ {} was created.", newRepo.getRepoName());
			
			
			logger.info("SpingJgitDemoApplication finished");
		}
		catch(Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}

}
