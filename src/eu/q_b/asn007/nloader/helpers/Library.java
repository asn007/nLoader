package eu.q_b.asn007.nloader.helpers;

import java.io.File;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.multiclient.GameServer;

public class Library {

	private String name;
	
	public Library(String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	public String getLibraryFile(boolean isMinecraft, GameServer gs) {
		// TODO isMinecraft
		return BaseProcedures.getWorkingDirectoryFor(gs) + File.separator + "bin" + File.separator + this.name() + ".jar";
	}
	
	

}
