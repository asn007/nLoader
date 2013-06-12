package eu.q_b.asn007.nloader.multiclient;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import eu.q_b.asn007.nloader.xml.*;

public class GameServerLoader {

	public static ArrayList<GameServer> getServers(String string,
			XMLParser parser) {
		ArrayList<GameServer> gservs = new ArrayList<GameServer>();
		NodeList nl = parser.getDocumentRoot().getElementsByTagName(string);
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				GameServer e = getServer(el);
				gservs.add(e);
			}
		}
		return gservs;
	}

	private static GameServer getServer(Element servelem) {
		String name = servelem.getAttribute("name");
		String address = servelem.getAttribute("address");
		boolean isSpoutcraft = servelem.getAttribute("spoutcraft").equals("true");
		return new GameServer(name, address, servelem.getAttribute("servicename"), isSpoutcraft);
	}


}
