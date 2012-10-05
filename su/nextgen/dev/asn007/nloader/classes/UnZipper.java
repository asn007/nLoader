package su.nextgen.dev.asn007.nloader.classes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipper {
	
	 private final static int BUFFER = 10241024;  
	    /** 
	     * @param args 
	     */  
	    
	    public File recursiveUnzip(File inFile, File outFolder)  
	    {  
	        BaseLogger.write("Extracting archive: " + inFile.getName());
	    	try  
	         {  
	              this.createFolder(outFolder, true);  
	              BufferedOutputStream out = null;  
	              ZipInputStream  in = new ZipInputStream((new FileInputStream(inFile)));  
	              ZipEntry entry;  
	              while((entry = in.getNextEntry()) != null)  
	              {  
	                   BaseLogger.write("Processing entry: " + entry);  
	                   int count;  
	                   byte data[] = new byte[BUFFER];  
	                     

	                   File newFile = new File(outFolder.getPath() + "/" + entry.getName());  
	                   Stack<File> pathStack = new Stack<File>();  
	                   File newNevigate = newFile.getParentFile();  
	                   while(newNevigate != null){  
	                       pathStack.push(newNevigate);  
	                       newNevigate = newNevigate.getParentFile();  
	                   }  
	 
	                   while(!pathStack.isEmpty()){  
	                       File createFile = pathStack.pop();  
	                       this.createFolder(createFile, true);  
	                   }  
	                   if(!entry.isDirectory()){  
	                         out = new BufferedOutputStream(  
	                                   new FileOutputStream(newFile),BUFFER);  
	                         while ((count = in.read(data,0,BUFFER)) != -1){  
	                              out.write(data,0,count);  
	                         }  
	                         this.cleanUp(out);
	                       this.createFolder(new File(entry.getName()), true);  
	                   }  
	              }  
	              this.cleanUp(in);  
	              return outFolder;  
	         }
	    	catch(Exception e)
	         {  
	              e.printStackTrace();  
	              return inFile;  
	         }  
	    }  
	      
	    public void removeAllZipFiles(File folder)
	    {  
	        String[] files = folder.list();
	        for(String file: files){  
	            File item = new File(folder.getPath() + File.separator + file);
	            
	           	if(item.exists() && item.getName().toLowerCase().endsWith(".zip"))
	            {
	            	BaseLogger.write("Processing entry: " + item.getName());
	                item.delete();   
	            }  
	        }  
	    }  
	      
	      
	    private void createFolder(File folder, boolean isDriectory)
	    {  
	        if(isDriectory)
	        {  
	            folder.mkdir();  
	        }  
	    }  
	      
	    private void cleanUp(InputStream in) throws Exception
	    {
	    
	         in.close();  
	    }  
	      
	    private void cleanUp(OutputStream out) throws Exception
	    {  
	         out.flush();  
	         out.close();  
	    }  

}
