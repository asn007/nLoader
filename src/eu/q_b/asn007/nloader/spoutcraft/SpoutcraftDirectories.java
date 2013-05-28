package eu.q_b.asn007.nloader.spoutcraft;

import java.io.File;
import eu.q_b.asn007.nloader.*;

public class SpoutcraftDirectories {
	private final File backupDir = new File(BaseProcedures.getWorkingDirectory(), "backups");
	private final File binDir = new File(BaseProcedures.getWorkingDirectory(), "bin");
	private final File binCacheDir = new File(binDir, "cache");
	private final File spoutcraftDir = new File(BaseProcedures.getWorkingDirectory(), "config");
	private final File savesDir = new File(BaseProcedures.getWorkingDirectory(), "saves");
	private final File updateDir = new File(BaseProcedures.getWorkingDirectory(), "temp");
	private final File skinDir = new File(BaseProcedures.getWorkingDirectory(), "skins");

	public final File getBinDir() {
		return binDir;
	}

	public final File getBinCacheDir() {
		return binCacheDir;
	}

	public final File getBackupDir() {
		return backupDir;
	}

	public final File getSpoutcraftDir() {
		return spoutcraftDir;
	}

	public final File getSavesDir() {
		return savesDir;
	}

	public final File getUpdateDir() {
		return updateDir;
	}

	public final File getSkinDir() {
		return skinDir;
	}
}

