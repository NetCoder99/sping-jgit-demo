package com.example.demo.utilites;

import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.enums.OSType;

public class EnvUtilities {
	static Logger logger = LoggerFactory.getLogger(EnvUtilities.class);

	public static OSType GetOS() throws Exception {
		logger.info("Operating system: {}", System.getProperty("os.name"));
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			return OSType.WINDOWS;
		}
		else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return OSType.LINUX;
		} 
		else {
			throw new Exception("Unknown Operating system.");
		}
	}

	public static void ShowProcessEnv() throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder();        
		Map<String, String> environment = processBuilder.environment();
		Map<String, String> treeMap = new TreeMap<>(environment);		
		treeMap.forEach((key, value) -> {
			logger.info("{}:{}", key, value);
		});		
	}

	public static void TestProcessEnv() throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder();        
		Map<String, String> environment = processBuilder.environment();
		environment.put("GREETING", "Hola Mundo");

		processBuilder.command("/bin/bash", "-c", "echo $GREETING");
		
		Process process = processBuilder.start();
		
		String stdOut = getInputAsString(process.getInputStream());
		String stdErr = getInputAsString(process.getErrorStream());

		logger.info("+++++++++++++++++++++++++++++++++");
		logger.info(stdOut);
		logger.info("+++++++++++++++++++++++++++++++++");
		logger.info(stdErr);
		
		
	}
	
	private static String getInputAsString(InputStream is)
	{
	   try(java.util.Scanner s = new java.util.Scanner(is)) 
	   { 
	       return s.useDelimiter("\\A").hasNext() ? s.next() : ""; 
	   }
	}	
}
