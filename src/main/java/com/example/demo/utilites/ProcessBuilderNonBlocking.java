package com.example.demo.utilites;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.models.ProcessResponse;

public class ProcessBuilderNonBlocking {
	static Logger logger = LoggerFactory.getLogger(ProcessBuilderNonBlocking.class);

	// -------------------------------------------------------------------------------------------------
	public static ProcessResponse Execute(String[] cmdArgs) throws Exception {
        try {
        	logger.info("++++ ProcessBuilderNonBlocking.Execute Started ++++");
			ProcessResponse response = new ProcessResponse();
			response.setCmdArgs(cmdArgs);
			response.setExecStartTime(LocalDateTime.now());
        	
    		ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(cmdArgs);
        	
        	Process       process   = processBuilder.start();
        	InputStream   inpStream = process.getInputStream();
        	InputStream   errStream = process.getErrorStream();

        	// give the process a half second to start up
            process.waitFor(500, TimeUnit.MILLISECONDS);
        	
            // check if process is still running, possibly stuck on prompt 
            WaitForProcessToComplete(process, 3);

            // fetch the current output from the process
            String inpString = GetInputBufferString(inpStream);
            logger.info("++++ inpString: \n" + inpString);

            // check if we might be stuck on a prompt
            if (process.isAlive()) {
                CheckIfPrompt(process, inpString);
                WaitForProcessToComplete(process, 1);
            }
            
            // if the process is still running than we are getting an unexpected 
            // response, kill the process and throw an exception
            if (process.isAlive()) {
            	logger.info("++++ Destroying process");
            	process.destroy();
            	throw new Exception("Process did not complete as exepected");
            }

            response.setRetCd(process.exitValue());
            response.setGoodResponse(inpString);
            response.setErrResponse(GetInputBufferString(errStream));
			response.setExecEndTime(LocalDateTime.now());

			logger.info("++++ ProcessBuilderNonBlocking.Execute Finished ++++");
            return response;
        } catch (Exception e) {
        	throw e;
        }		
	}
	
	// -------------------------------------------------------------------------------------------------
	private static boolean WaitForProcessToComplete(Process process, int timeOut) {
		LocalDateTime endTime    = LocalDateTime.now().plusSeconds(timeOut);		
		while (LocalDateTime.now().compareTo(endTime) < 0) {
			if (!process.isAlive()) {
				return false;
			}
		}
		return true;
	}
	
	// -------------------------------------------------------------------------------------------------
	private static String GetInputBufferString(InputStream inpStream) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	byte[] temp2 = inpStream.readNBytes(inpStream.available());
        for(byte tbyte : temp2) {
        	sb.append((char)tbyte);
        }
        return sb.toString();
		
	}

	// -------------------------------------------------------------------------------------------------
	private static void CheckIfPrompt(Process process, String inpString) throws Exception {
		logger.info("++++ CheckIfPrompt: {} \n", inpString);
		String testStr1 = "Are You Sure?[Y/N]:";
		String testStr2 = "Hello there";
		if (inpString.contains(testStr1) || inpString.contains(testStr2)) {
			logger.info("++++ Responding to prompt: {}", testStr1);
			OutputStream outStream = process.getOutputStream();
			outStream.write("N".getBytes());
			outStream.close();
		}
	}
}
