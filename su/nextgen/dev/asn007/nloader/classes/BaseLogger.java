package su.nextgen.dev.asn007.nloader.classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BaseLogger {
	 private static String logFile = BaseProcedures.getWorkingDirectory() + File.separator  + "launcher.log";
	 private static String logFileD = BaseProcedures.getWorkingDirectory() + File.separator;
	    private final static DateFormat df = new SimpleDateFormat ("dd.MM.yyyy  hh:mm:ss ");

	    public BaseLogger() 
	    {  
	    	File f = new File(logFile);
	    	File fD = new File(logFileD);
	    	if(!fD.exists())
	    	{
	    		fD.mkdirs();
	    	}
	    	if(!f.exists())
	    	{
	    		try {
					f.createNewFile();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	    	}
	    	System.out.println("Logging system initialised!");
	    }
	    
	    public static void setLogFilename(String filename) {
	        logFile = filename;
	        new File(filename).delete();

	        try {
	            write("LOG file: " + filename);
	        }
	        catch (Exception e) { 
	           e.printStackTrace();
	        }
	        
	    }
	    
	    public static void write(String msg) {
	        write(logFile, msg);
	    }
	    
	    public static void write(Exception e) {
	        write(logFile, stack2string(e));
	    }

	    public static void write(String file, String msg) {
	        try {
	            Date now = new Date();
	            String currentTime = BaseLogger.df.format(now); 
	            FileWriter aWriter = new FileWriter(file, true);
	            aWriter.write("[" + currentTime + "]" + " " + msg 
	                    + System.getProperty("line.separator"));
	            System.out.println("[ " + currentTime + "]" + " " + msg);
	            aWriter.flush();
	            aWriter.close();
	        }
	        catch (Exception e) {
	            System.out.println(stack2string(e));
	        }
	    }
	    
	    private static String stack2string(Exception e) {
	        try {
	            StringWriter sw = new StringWriter();
	            PrintWriter pw = new PrintWriter(sw);
	            e.printStackTrace(pw);
	            return "------\r\n" + sw.toString() + "------\r\n";
	        }
	        catch(Exception e2) {
	            return "bad stack2string";
	        }
	    }
}
