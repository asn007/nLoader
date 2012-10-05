package su.nextgen.dev.asn007.nloader.classes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Random;

import su.nextgen.dev.asn007.nloader.gui.ProgressBar;

public class BaseProcedures {
	
	private static File workDir = null;
	private static File binDir = new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator);
	private static File nativesDir = new File(BaseProcedures.getWorkingDirectory() + File.separator + "bin" + File.separator + "natives" + File.separator);

	public static File getWorkingDirectory() {

		workDir = getWorkingDirectory(LauncherConf.minecraftDir);

		return workDir;
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
				workingDirectory = new File(applicationData, applicationName + '/');
			else
				workingDirectory = new File(userHome, applicationName + '/');
			break;
		case 3:
			workingDirectory = new File(userHome,
					"Library/Application Support/" + applicationName);
			break;
		default:
			workingDirectory = new File(userHome, applicationName + '/');
		}
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
			throw new RuntimeException(
					"The working directory could not be created: "
							+ workingDirectory);
		return workingDirectory;
	}

	public static OS getPlatform() {
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

	
	public static int[] getOnline(String ip, String port) {
		try {
		Socket var3 = null;
		DataInputStream var4 = null;
		DataOutputStream var5 = null;

		var3 = new Socket();
		var3.setSoTimeout(3000);
		var3.setTcpNoDelay(true);
		var3.setTrafficClass(18);
		var3.connect(new InetSocketAddress(ip, Integer.parseInt(port)), 3000);
		var4 = new DataInputStream(var3.getInputStream());
		var5 = new DataOutputStream(var3.getOutputStream());
		var5.write(254);

		if (var4.read() != 255) {
			throw new IOException("Bad message");
		}

		String var6 = readString(var4, 256);
		char[] var7 = var6.toCharArray();
		var6 = new String(var7);
		String[] var27 = var6.split("\u00a7");
		var6 = var27[0];

		int var9 = Integer.parseInt(var27[1]);
		int var10 = Integer.parseInt(var27[2]);

		//BaseLogger.write("Online: " + var9 + " of " + var10);
		return new int[] {var9, var10};
		} catch(Exception e)
		{
			return null;
		}

	}

	public static String readString(DataInputStream par0DataInputStream,
			int par1) throws IOException {
		short var2 = par0DataInputStream.readShort();

		if (var2 > par1) {
			throw new IOException(
					"Received string length longer than maximum allowed ("
							+ var2 + " > " + par1 + ")");
		} else if (var2 < 0) {
			throw new IOException(
					"Received string length is less than zero! Weird string!");
		} else {
			StringBuilder var3 = new StringBuilder();

			for (int var4 = 0; var4 < var2; ++var4) {
				var3.append(par0DataInputStream.readChar());
			}

			return var3.toString();
		}
	}
	
	// Deprecated, because works from time to time. There's better alternative:
	// isOnline();
	@Deprecated
	public static boolean isAval() {
		InetAddress iadr;
		try {
			iadr = InetAddress.getByName("google.ru");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			BaseLogger.write("System is in offline mode!");
			return false;
		}
		try {
			return iadr.isReachable(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isOnline() {

		BaseLogger.write("Checking internet connection...");
		URL url;
		URLConnection urlconn;

		try {
			url = new URL("http://nextgen.su/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			BaseLogger.write(e);
			BaseLogger.write("System is in offline mode!");
			return false;
		}

		try {
			urlconn = url.openConnection();
			urlconn.connect();
			BaseLogger.write("System is online!");
			return true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			BaseLogger.write("System is in offline mode!");
			return false;
		}

	}
	
	public static String buildRandomSession()
	{
		String s = "";
		for (int i = 0; i < 22; i++) {
			Random r = new Random(System.currentTimeMillis()
					+ System.nanoTime() * System.currentTimeMillis() + i + Math.round(Math.random()));
			s = s + r.nextInt(9);
			r = null;
		}
		return s;
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
		}catch(Exception e)
		{
			return defaultVar;
		}
	}

	public static void openLink(URI uri) {
		try {
			Object o = Class.forName("java.awt.Desktop")
					.getMethod("getDesktop", new Class[0])
					.invoke(null, new Object[0]);
			o.getClass().getMethod("browse", new Class[] { URI.class })
					.invoke(o, new Object[] { uri });
		} catch (Throwable e) {
			System.err.println("Failed to open link " + uri.toString());
			e.printStackTrace();
		}
	}
	
	public static boolean isClientCorrect() {
		if(new File(getBinFolder() + File.separator + "minecraft.jar").exists() && new File(getBinFolder() + File.separator + "jinput.jar").exists() && new File(getBinFolder() + File.separator + "lwjgl.jar").exists() && new File(getBinFolder() + File.separator + "lwjgl_util.jar").exists()) return true;
		else return false;
	}
	
	public static URI toURI(URL url)  {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			BaseLogger.write(e);
			return null;
		}
	}
	
	public static File getBinFolder() {
		if(!binDir.exists()) binDir.mkdirs();
		return binDir;
		
	}
	
	public static void download(URL url, File f) throws Exception {
		
		File f2 = f;
		if (f2.exists())
			f2.delete();
		if (!f2.exists()) {
			try {
				f2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		URLConnection connection = url.openConnection();

		long down = connection.getContentLength();

		long downm = f2.length();

		if (downm != down) {

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());

			FileOutputStream fw = new FileOutputStream(f2);

			byte[] b = new byte[1024];
			int count = 0;

			while ((count = bis.read(b)) != -1) {

				fw.write(b, 0, count);

			}
			fw.close();
		}

		else
			return;

	}
	
	public static void writeString(String file, String text) {
		try {
		File f = new File(file);
		FileWriter fw = new FileWriter(f);
		fw.write(text);
		fw.close();
		System.gc();
		}catch(Exception e) {
			BaseLogger.write("Error while writing string to a file!");
			BaseLogger.write(e);
			
		}
	}
	
	public static void writeVersionFile(String version) {
		try {
		File f = new File(BaseProcedures.getBinFolder() + File.separator+ "build");
		FileWriter fw = new FileWriter(f);
		fw.write(version);
		fw.close();
		System.gc();
		}catch(Exception e) {
			BaseLogger.write("Error while writing version file!");
			BaseLogger.write(e);
			
		}
	}
	
public static void download(URL url, File f, ProgressBar p) throws Exception {
		
		File f2 = f;
		if(!f2.getParentFile().exists()) f2.mkdirs();
		if (f2.exists())
			f2.delete();
		if (!f2.exists()) {
			try {
				f2.createNewFile();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		URLConnection connection = url.openConnection();
		
		long down = connection.getContentLength();
		p.setMaximum(100);
		long downm = f2.length();

		if (downm != down) {

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			BufferedInputStream bis = new BufferedInputStream(
					conn.getInputStream());

			FileOutputStream fw = new FileOutputStream(f2);

			byte[] b = new byte[1024];
			int count = 0;
			long total = 0;

			while ((count = bis.read(b)) != -1) {
				total += count;
				fw.write(b, 0, count);
				//BaseLogger.write(((Integer)Math.round((100 * total) / down)).toString());
				p.setValue(Math.round((100 * total) / down));
			}
			fw.close();
		}

		else
			return;

	}
	
	public static String iPostCreator(String URL, String param) {

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
			return null;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public static String iGetCreator(String URL, String param) throws Exception {
		URL localURL;
		localURL = new URL(URL + "?" + param);

		BufferedReader localBufferedReader = new BufferedReader(
				new InputStreamReader(localURL.openStream()));
		String str2 = localBufferedReader.readLine();
		return new String(str2);

	}
	
	public static URL toURL(String url)
	{
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			BaseLogger.write(e);
			return null;
		}
	}

	public static File getNativesFolder() {
		if(!nativesDir.exists()) nativesDir.mkdirs();
		return nativesDir;
	}

	public static void doCleanUp() {
		try {
			recursiveDelete(BaseProcedures.getBinFolder());
			if(LauncherConf.removeMods) recursiveDelete(new File(BaseProcedures.getWorkingDirectory() + File.separator + "mods"));
		} catch (IOException e) {
			BaseLogger.write("Error occured while doing the clean-up!");
			BaseLogger.write(e);
		}
		
	}
	
	public static void recursiveDelete(File file) throws IOException {
		if(!file.exists()) return;
		if (file.isDirectory()) {
			if (file.list().length == 0) file.delete();
			else {
				String files[] = file.list();

				for (String temp : files) {
					File fileDelete = new File(file, temp);
					recursiveDelete(fileDelete);
				}
				if (file.list().length == 0) file.delete();	
			}
		}
		else file.delete();
	}
}
