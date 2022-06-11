package com.example.demo.utilites;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class StreamGobbler implements Runnable {

    private PrintStream printStream;
    private Scanner inScanner;
    //private Consumer<String> consumer;

    public StreamGobbler(PrintStream printStream, InputStream inStream) {
        this.printStream = printStream;
        inScanner = new Scanner(new BufferedInputStream(inStream));
        //this.consumer = consumer;
    }

    @Override
    public void run() {
        while (inScanner.hasNextLine()) {
            String line = inScanner.nextLine();
            System.out.printf("%s: %s%n", line);
         }    	
    }
}
