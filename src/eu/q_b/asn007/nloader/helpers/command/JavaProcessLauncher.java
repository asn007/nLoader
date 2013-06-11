 package eu.q_b.asn007.nloader.helpers.command;
 
 import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.q_b.asn007.nloader.BaseProcedures;
 
 public class JavaProcessLauncher
 {
   private final String jvmPath;
   private final List<String> commands;
   private File directory;
 
   public JavaProcessLauncher(String jvmPath, String[] commands)
   {
     if (jvmPath == null) jvmPath = BaseProcedures.getJavaExecutable();
     this.jvmPath = jvmPath;
     this.commands = new ArrayList<String>(commands.length);
     addCommands(commands);
   }
 
   public JavaProcess start() throws IOException {
     List<String> full = getFullCommands();
     return new JavaProcess(full, new ProcessBuilder(full).directory(this.directory).redirectErrorStream(true).start());
   }
 
   public List<String> getFullCommands() {
     List<String> result = new ArrayList<String>(this.commands);
     result.add(0, getJavaPath());
     return result;
   }
 
   public List<String> getCommands() {
     return this.commands;
   }
 
   public void addEscapedCommand(String command) {
     this.commands.add(escapeArgument(command));
   }
 
   public void addCommands(String[] commands) {
     List<String> args = Arrays.asList(commands);
 
     for (int i = 1; i < args.size(); i++) {
       args.set(i, escapeArgument((String)args.get(i)));
     }
 
     this.commands.addAll(args);
   }
 
   public JavaProcessLauncher directory(File directory) {
     this.directory = directory;
 
     return this;
   }
 
   public File getDirectory() {
     return this.directory;
   }
 
   public static String escapeArgument(String input) {
     String result = "";
 
     if ((input.indexOf(' ') >= 0) || (input.indexOf('\t') >= 0) || (input.indexOf("*") > 0)) {
       if (input.charAt(0) != '"') {
         result = result + "\"";
         result = result + input;
         if (input.endsWith("\\")) {
           result = result + "\\";
         }
         result = result + "\"";
       } else if (input.endsWith("\"")) {
         result = result + input;
       } else {
         throw new IllegalArgumentException("Illegal unmatched quote in commands");
       }
     }
     else result = input;
 
     return result;
   }
 
   public static String buildCommands(List<String> commands) {
     StringBuilder builder = new StringBuilder(80);
 
     for (int i = 0; i < commands.size(); i++) {
       if (i > 0) {
         builder.append(' ');
       }
 
       builder.append((String)commands.get(i));
     }
 
     return builder.toString();
   }
 
   protected String getJavaPath() {
     return this.jvmPath;
   }
 
   public String toString()
   {
     return "JavaProcessLauncher[commands=" + this.commands + ", java=" + this.jvmPath + "]";
   }
 }

