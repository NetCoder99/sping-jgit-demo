package com.example.demo.utilites;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.SpingJgitDemoApplication;

public class GitBatchCommands {
	static Logger logger = LoggerFactory.getLogger(SpingJgitDemoApplication.class);

	public static void ShowProcessEnv() throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder();        
		Map<String, String> environment = processBuilder.environment();
		Map<String, String> treeMap = new TreeMap<>(environment);		
		treeMap.forEach((key, value) -> {
			logger.info("{}:{}", key, value);
		});		
		
		
		environment.put("GREETING", "Hola Mundo");
		processBuilder.command("/bin/bash", "-c", "echo $GREETING");
		Process process = processBuilder.start();
		
	}
	
	
	
}
