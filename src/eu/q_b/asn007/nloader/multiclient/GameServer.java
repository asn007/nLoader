package eu.q_b.asn007.nloader.multiclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import eu.q_b.asn007.nloader.*;

public class GameServer  {

	private String name;
	private String address;
	private String toString;
	private boolean isSpoutCraft;
	private String serviceName;

	public GameServer() {}
	
	public GameServer(String name2, String address2, String serviceName, boolean isSpoutcraft2) {
		this.name = name2;
		this.address = address2;
		this.isSpoutCraft = isSpoutcraft2;
		this.serviceName = serviceName;
		this.toString = getOnlineString();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isSpoutCraft() {
		return isSpoutCraft;
	}
	public void setSpoutCraft(boolean isSpoutCraft) {
		this.isSpoutCraft = isSpoutCraft;
	}
	
	public String getOnlineString() {
		try {
			String[] online = getOnline();
			if(online == null || online[1] == null || online[1].equals("0")) throw new Exception("Server is offline!");
			else return this.name + " (" + online[0] + "/" + online[1] + ")";
		} catch(Exception ex) {
			return this.name + " (сервер отключен)";
		}
	}
	
	public String toString() {
		if(this.toString == null) this.toString = getOnlineString();
		return this.toString;
	}
	
	public String[] getOnline() throws Exception {
		Socket var2 = null;
        DataInputStream var3 = null;
        DataOutputStream var4 = null;
        try {
            var2 = new Socket();
            var2.setSoTimeout(3000);
            var2.setTcpNoDelay(true);
            var2.setTrafficClass(18);
            var2.connect(new InetSocketAddress(this.address.split(":")[0], Integer.parseInt(this.address.split(":")[1])), 3000);
            var3 = new DataInputStream(var2.getInputStream());
            var4 = new DataOutputStream(var2.getOutputStream());
            var4.write(254);
            var4.write(1);
            if (var3.read() != 255) {
                throw new IOException("Bad message");
            }
            String var5 = BaseProcedures.readString(var3, 256);
            char[] var6 = var5.toCharArray();
            var5 = new String(var6);
            String[] var26;
            if (var5.startsWith("\u00a7") && var5.length() > 1) {
                var26 = var5.substring(1).split("\u0000");
                //System.out.println(var26[0]);
                  // System.out.println("MOTD: " + var26[3]);
                   //System.out.println("PROTOCOL: " + var26[1]);
                    //System.out.println("Game version: " + var26[2]);
                    //var8 = Integer.parseInt(var26[4]);
                    //var9 = Integer.parseInt(var26[5]);
                    //System.out.println(var26[4] + "/" + var26[5]);
                return new String[] { var26[4], var26[5]};
            }
            else {
                var26 = var5.split("\u00a7");
                var5 = var26[0];
                try {
                    return new String[] {var26[1], var26[2]};
                }
                catch (Exception var24) {
                    return new String[] { "0", "0" };
                }
            }
        }
        finally {
            try {
                if (var3 != null) {
                    var3.close();
                }
            } catch (Throwable var23) {}
            try {
                if (var4 != null) {
                    var4.close();
                }
            }
            catch (Throwable var22) {}
            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (Throwable var21) {}
        }


	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	
}
