package eu.q_b.asn007.nloader.multiclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import eu.q_b.asn007.nloader.*;

public class GameServer extends BasicGameServer {

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
			if(online == null || online[1].equals("0")) throw new Exception("Server is offline!");
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
		try {
			Socket var3 = null;
			DataInputStream var4 = null;
			DataOutputStream var5 = null;

			var3 = new Socket();
			var3.setSoTimeout(3000);
			var3.setTcpNoDelay(true);
			var3.setTrafficClass(18);
			var3.connect(new InetSocketAddress(this.address.split(":")[0], Integer.parseInt(this.address.split(":")[1])),
					3000);
			var4 = new DataInputStream(var3.getInputStream());
			var5 = new DataOutputStream(var3.getOutputStream());
			var5.write(254);

			if (var4.read() != 255) {
				var3.close();
				throw new IOException("Bad message");
			}

			String var6 = BaseProcedures.readString(var4, 256);
			char[] var7 = var6.toCharArray();
			var6 = new String(var7);
			String[] var27 = var6.split("\u00a7");
			var6 = var27[0];

			int var9 = Integer.parseInt(var27[1]);
			int var10 = Integer.parseInt(var27[2]);
			var3.close();
			return new String[] { var9 + "", var10 + "" };
		} catch (Exception e) {
			throw new Exception("Failed to get server online!");
		}

	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	
}
