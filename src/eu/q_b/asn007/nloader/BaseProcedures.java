package eu.q_b.asn007.nloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import eu.q_b.asn007.nloader.fx.*;
import eu.q_b.asn007.nloader.helpers.*;

public class BaseProcedures {

	private static FileWriter logWriter;
	private static Date now;
	private final static DateFormat df = new SimpleDateFormat ("dd.MM.yyyy  hh:mm:ss ");
	private final static String sep = System.getProperty("line.separator");
	private static String currentTime;
	
	
	public static String runGET(String URL, String param) {
		try {
			URL localURL;
			localURL = new URL(URL + "?" + param);

			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localURL.openStream()));
			StringBuffer sb = new StringBuffer();
			String result;
			while ((result = localBufferedReader.readLine()) != null)
				sb.append(result + "\n");
			return sb.toString().substring(0, sb.length() - 1); // Finally fixed this :3
		} catch (Exception e) {
			log("Error while running GET request! Returning empty string...",
							BaseProcedures.class);
			log(stack2string(e),
					BaseProcedures.class);
			return "";
		}

	}

	public static String runPOST(String URL, String param) {

		HttpURLConnection connection = null;
		try {
			URL url = new URL(URL);
			connection = (HttpURLConnection) url.openConnection();

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			connection.connect();

			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			StringBuffer response = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			rd.close();

			String str1 = response.toString();

			return str1;
		} catch (Exception e) {
			return "";
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}
	
	public static void setupLogger(File logFile) {
		try {
			if(!logFile.exists()) {
				//logFile.mkdirs();
				logFile.createNewFile();
			}
			if(readFileAsString(logFile.toString(), "").split("\n").length >= 500 /*To make logs more readable.. Approx. 5-7 launches*/) {
				logFile.delete();
				logFile.createNewFile();
			}
			logWriter = new FileWriter(logFile, true);
		} catch(Exception e) {
			System.out.println("[enCore] failed to initialize logger...");
			System.out.println(stack2string(e));
		}
	}
	
	public static String stack2string(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
			return "------\r\n" + sw.toString() + "------\r\n";
		} catch (Exception e2) {
			return "Bad stack2string";
		}
	}
	
	public static void download(URL url, final File f, final Label p, final ProgressBar progressbar)
			throws Exception {
		f.mkdirs();
		f.delete();
		f.createNewFile();
		URLConnection connection = url.openConnection();
		final long down = connection.getContentLength();
		long downm = f.length();
		if (downm != down) {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());
			FileOutputStream fw = new FileOutputStream(f);
			byte[] b = new byte[1024];
			int count = 0;
			long total = 0;
			while ((count = bis.read(b)) != -1) {
				total += count;
				fw.write(b, 0, count);
				final long t = total;
				Platform.runLater(new Runnable(){
					public void run() {
						p.setText(f.getName() + " (" + Math.round((100 * t) / down) + "%)");
						progressbar.setProgress((((100 * t) / down) / 100D));
					}
				});
				
			}
			fw.close();
		}
	}
	
	private static File workDir = null;
	
	public static File getWorkingDirectory() {

		workDir = getWorkingDirectory(LauncherConf.mcDir);

		return workDir;
	}
	
	@SafeVarargs
	public static <T> T[] concatAll(T[] first, T[]... rest) {
		  int totalLength = first.length;
		  for (T[] array : rest) {
		    totalLength += array.length;
		  }
		  T[] result = Arrays.copyOf(first, totalLength);
		  int offset = first.length;
		  for (T[] array : rest) {
		    System.arraycopy(array, 0, result, offset, array.length);
		    offset += array.length;
		  }
		  return result;
		}
	
	public static void recursiveDelete(File file) throws IOException {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			if (file.list().length == 0)
				file.delete();
			else {
				String files[] = file.list();

				for (String temp : files) {
					File fileDelete = new File(file, temp);
					recursiveDelete(fileDelete);
				}
				if (file.list().length == 0)
					file.delete();
			}
		} else
			file.delete();
	}
	
	public static File getWorkingDirectory(String applicationName) {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform().ordinal()) {
		case 0:
		case 1:
			workingDirectory = new File(userHome, applicationName + '/');
			break;
		case 2:
			String applicationData = System.getenv("APPDATA");
			if (applicationData != null)
				workingDirectory = new File(applicationData,
						applicationName + '/');
			else
				workingDirectory = new File(userHome, applicationName + '/');
			break;
		case 3:
			workingDirectory = new File(userHome,
					"Library" +File.separator + "Application Support" + File.separator + applicationName);
			break;
		default:
			workingDirectory = new File(userHome, applicationName + '/');
		}
		workingDirectory.mkdirs();
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
			throw new RuntimeException(
					"The working directory could not be created: "
							+ workingDirectory);
		return workingDirectory;
	}
	
	public static String getMD5(File f) {
		try {
			return calculateHash(MessageDigest.getInstance("MD5"), f.toString());
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String calculateHash(MessageDigest algorithm, String fileName)
			throws Exception {
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DigestInputStream dis = new DigestInputStream(bis, algorithm);

		while (dis.read() != -1)
			;
		byte[] hash = algorithm.digest();
		dis.close();
		return byteArray2Hex(hash);
	}

	public static String byteArray2Hex(byte[] hash) {
		@SuppressWarnings("resource")
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		// formatter.close();
		return formatter.toString();
	}

	public static void writeString(String text, File file) {
		try {
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		fw.write(text);
		fw.close();
		} catch(IOException ignored){}
		
	}
	
	public static String readFileAsString(String filePath, String defaultVar) {
		try {
			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {
			return defaultVar;
		}
	}
	
	public static  OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win"))
			return OS.windows;
		if (osName.contains("mac"))
			return OS.macos;
		if (osName.contains("solaris"))
			return OS.solaris;
		if (osName.contains("sunos"))
			return OS.solaris;
		if (osName.contains("linux"))
			return OS.linux;
		if (osName.contains("unix"))
			return OS.linux;
		return OS.unknown;
	}

	public static enum OS {
		linux, solaris, windows, macos, unknown;
	}


	
	
	public static URL toURL(String url) {
		try {
			return new URL(url.replace(" ", "%20"));
		} catch (Exception e) {
			log("failed to transform String to URL.. Sorry..", BaseProcedures.class);
			return null;
		}
	}
	
	public static URI toURI(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			log("failed to transform URL to URI.. Sorry..", BaseProcedures.class);
			return null;
		}
	}
		
		public static void log(String text, Class<?> c) {
			if(logWriter == null) setupLogger(new File(getWorkingDirectory() + File.separator + LauncherConf.logFileName));
			try {
				now = new Date();
				currentTime = df.format(now); 
				String nm = "";
				if(c.getSimpleName().equals("")) nm = "UNDEF"; else nm = c.getSimpleName();
				logWriter.write("[" + currentTime + "][" + nm + "] " + text + sep);
				System.out.println("[ " + currentTime + "][" + nm + "] " + text);
				logWriter.flush();
			} catch(Exception e) {
				System.out.println(stack2string(e));
			}
		}

		public static Object[] toArray(List<Object> url) {
			Object[] arr = new Object[url.size()];
			for(int i = 0; i < url.size(); i++) {
				arr[i] = url.get(i);
			}
			return arr;
		}

		public static boolean isSpoutCraft() {
			return LauncherConf.isSpoutCraft;
			// TODO: add spoutcraft autodetection
		}

		public static ArrayList<String> getDirectoriesList() {
			ArrayList<String> t = new ArrayList<String>();
			String get = runGET(LauncherConf.downloadURL + "verifier.php", "act=dirs");
			if(get == null || !get.contains("<br />")) {
				Platform.runLater(new Runnable(){
					public void run() {
						new ModalWindow(Main.loc.getString("nloader.generic.oops"), Main.loc.getString("nloader.window.dirlist_failed"));
						System.exit(0);						
					}
				});

			} else {
				String[] s = get.split("<br />");
				for(String str: s) t.add(str);
			}
			
			return t;
		}
		
		public static List<File> addFiles(File dir) {
			File[] list = dir.listFiles();
			LinkedList<File> files = new LinkedList<File>();
			for(File f: list) {
				if(!f.isDirectory()) files.add(f);
				else addFiles(f, files);
			}
		    return files;
		}
		
		public static List<File> addFiles(File dir, List<File> t) {
			File[] list = dir.listFiles();
			for(File f: list) {
				if(!f.isDirectory()) t.add(f);
				else addFiles(f, t);
			}
		    return t;
		}

		public static List<Library> getLibraries() {
			List<Library> list = new ArrayList<Library>();
			File[] files = new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "lib").listFiles();
			for(File file: files) {
				if(file.getName().endsWith(".jar")) list.add(new Library(file.getName().substring(0, file.getName().length() - 4)));
			}
			return list;
		}

		public static void copyFile(File sourceFile, File destFile) throws IOException {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

		    FileChannel source = null;
		    FileChannel destination = null;

		    try {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    }
		    finally {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
		    }
		}

		public static File getAssetsDirectory() {
			return new File(getWorkingDirectory(), "assets");
		}
		
		//TODO Do the clean-up
		
	
}
