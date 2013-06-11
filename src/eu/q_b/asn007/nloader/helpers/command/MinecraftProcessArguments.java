 package eu.q_b.asn007.nloader.helpers.command;
 
  
 public enum MinecraftProcessArguments
 {
   LEGACY("%s %s", false, false), 
   USERNAME_SESSION("--username %s --session %s", false, false), 
   USERNAME_SESSION_VERSION("--username %s --session %s --version %s", false, true), 
   USERNAME_SESSION_UUID_VERSION("--username %s --session %s --uuid %s --version %s", true, true);
 
   private final String format;
   private final boolean useUuid;
   private final boolean useVersion;
 
   private MinecraftProcessArguments(String format, boolean useUuid, boolean useVersion) { 
	 this.format = format;
     this.useUuid = useUuid;
     this.useVersion = useVersion; 
   }

   public boolean isUseVersion() {
	   return useVersion;
   }

   public boolean isUseUuid() {
	   return useUuid;
   }

   public String getFormat() {
	   return format;
   }
 }
