package su.nextgen.dev.asn007.nloader.classes;

import java.io.File;
import java.util.ArrayList;

import su.nextgen.dev.asn007.nloader.classes.BaseProcedures.OS;



public class LauncherLoader {

	public static void main(String[] args) throws Exception
	{	
		try
		{
			String jarpath = LauncherLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			String memory = BaseProcedures.readFileAsString(BaseProcedures.getWorkingDirectory() + File.separator + "props", "1024");
			ArrayList<String> params = new ArrayList<String>();

			if (BaseProcedures.getPlatform() == OS.windows) params.add("javaw");
			else params.add("java");
			params.add("-Xmx" + memory + "m");
			params.add("-Xms" + memory + "m");
			params.add("-Dsun.java2d.noddraw=true");
			params.add("-Dsun.java2d.d3d=false");
			params.add("-Dsun.java2d.opengl=false");
			params.add("-Dsun.java2d.pmoffscreen=false");
			params.add("-classpath");
			params.add(jarpath);
			params.add("su.nextgen.dev.asn007.nloader.classes.NLoader");

			ProcessBuilder pb = new ProcessBuilder(params);
			Process process = pb.start();
			if (process == null) throw new Exception("!");
			System.exit(0);
		} catch (Exception e)
		{
			e.printStackTrace();
			NLoader.main(args);
		}
	}
	
}
