package eu.q_b.asn007.nloader;

import eu.q_b.asn007.nloader.minecraft.*;
import eu.q_b.asn007.nloader.spoutcraft.*;

public class BasicMinecraftLoader {

	public BasicMinecraftLoader(String user, String session) {
		System.setProperty("minecraft.applet.TargetDirectory", BaseProcedures.getWorkingDirectoryFor(Main._instance.currentServer).getAbsolutePath()); // Technic guys say this should work for directory changin'
		if(Main._instance.currentServer.isSpoutCraft())
			new SpoutcraftLoader(user, session);
		else 
			new MinecraftLoader(user, session);
		Main._instance.primaryStage.close();
	}

}
