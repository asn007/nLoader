package eu.q_b.asn007.nloader.multiclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class GameServerXMLParser {
	
	public Document dom;
	public ArrayList<GameServer> gservs;
	public GameServerXMLParser() {
		gservs = new ArrayList<GameServer>();
	}
	
	public void parseXML(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(file);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public ArrayList<GameServer> parseDocument(String nodeName){
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName(nodeName);
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				GameServer e = getServer(el);
				gservs.add(e);
			}
		}
		return gservs;
	}
	
	
	private GameServer getServer(Element servelem) {
		String name = servelem.getAttribute("name");
		String address = servelem.getAttribute("address");
		boolean isSpoutcraft = servelem.getAttribute("spoutcraft").equals("true");
		return new GameServer(name, address, servelem.getAttribute("servicename"), isSpoutcraft);
	}
}

