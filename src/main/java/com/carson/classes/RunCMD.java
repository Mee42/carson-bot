package com.carson.classes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunCMD {

	
	RunCMD() {
		
	}
	
	public static boolean run(String[] strings)  {
		try {
			
			ProcessBuilder builder = new ProcessBuilder(strings);
			
	        builder.redirectErrorStream(false);
	        
	        Process p;
			
			p = builder.start();
			System.out.println("exit with code 2");
	        System.exit(2);
	        
	        
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
	        }
	        
	        return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean run(String string) {
		return RunCMD.run(new String[] {string});
	}
	
	public static boolean runAdmin(String command) {
		
		
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
		
	}
	
}
