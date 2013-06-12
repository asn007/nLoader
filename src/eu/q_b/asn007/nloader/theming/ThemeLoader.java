package eu.q_b.asn007.nloader.theming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.image.Image;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.q_b.asn007.nloader.BaseProcedures;
import eu.q_b.asn007.nloader.LauncherConf;
import eu.q_b.asn007.nloader.Main;
import eu.q_b.asn007.nloader.xml.XMLParser;
public class ThemeLoader {

	public static Theme loadThemeFromXML(File file) {
		BaseProcedures.log("Parsing theme XML...", ThemeLoader.class);
		XMLParser parser = new XMLParser(file);
		String name;
		String author;
		String version;
		Image icon = null;
		int width;
		int height;
		NodeList nl = parser.getDocumentRoot().getElementsByTagName("name");
		name = ((Element)nl.item(0)).getTextContent();
		nl = parser.getDocumentRoot().getElementsByTagName("author");
		author = ((Element)nl.item(0)).getTextContent();
		nl = parser.getDocumentRoot().getElementsByTagName("version");
		version = ((Element)nl.item(0)).getTextContent();
		nl = parser.getDocumentRoot().getElementsByTagName("width");
		width = Integer.parseInt(((Element)nl.item(0)).getTextContent());
		nl = parser.getDocumentRoot().getElementsByTagName("height");
		height = Integer.parseInt(((Element)nl.item(0)).getTextContent());
		nl = parser.getDocumentRoot().getElementsByTagName("icon");
		String icN = ((Element)nl.item(0)).getTextContent();
		if(icN.equals("default")) icon = new Image(Main.class.getResourceAsStream("/eu/q_b/asn007/nloader/res/images/icon.png"));
		BaseProcedures.log("So we're loading theme named " + name + " v" + version + " created by " + author, ThemeLoader.class);
		BaseProcedures.log("Loading filelist for theme " + name + "...", ThemeLoader.class);
		nl = parser.getDocumentRoot().getElementsByTagName("files");
		Node node = nl.item(0);
		nl = node.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++) {
			if(nl.item(i) instanceof Element) {
			String fileString = ((Element)nl.item(i)).getTextContent();
			BaseProcedures.log("Checking " + fileString + " in " + name, ThemeLoader.class);
			File themeFile = new File(getDirectoryForTheme(name) + File.separator + fileString.replace("/", File.separator));
			if(!themeFile.exists() || themeFile.length() != BaseProcedures.getContentLength(LauncherConf.themeRoot + fileString)) {
				BaseProcedures.log("File " + fileString + " is missing or outdated! Redownloading...", ThemeLoader.class);
				try {
					BaseProcedures.download(BaseProcedures.toURL(LauncherConf.themeRoot + fileString), themeFile);
				} catch (Exception e) {
					BaseProcedures.log("Failed to download theme file: " + fileString + ". Launcher may look ugly!", ThemeLoader.class);
					BaseProcedures.log(BaseProcedures.stack2string(e), ThemeLoader.class);
				}
			} else BaseProcedures.log("File " + fileString + " is up to date!", ThemeLoader.class);
			}
		}
		if(icon == null)
			try {
				icon = new Image(new FileInputStream(new File(getDirectoryForTheme(name) + File.separator + icN)));
			} catch (FileNotFoundException e) {
				BaseProcedures.log("No icon file found! Reverting to default one...", ThemeLoader.class);
				icon = new Image(Main.class.getResourceAsStream("/eu/q_b/asn007/nloader/res/images/icon.png"));
			}
		return new Theme(name, author, version, width, height, icon);
	}
	
	public static File getDirectoryForTheme(Theme thm) {
		return new File(BaseProcedures.getWorkingDirectory() + File.separator + "nloader-themes" + File.separator + thm.THEME_NAME);
	}
	
	public static File getDirectoryForTheme(String thmnm) {
		return new File(BaseProcedures.getWorkingDirectory() + File.separator + "nloader-themes" + File.separator + thmnm);
	}
	
	public static String getStyleSheetFromTheme(Theme thm) {
		if(thm == null) return "/style.css";
		else {
			try {
				return new File(getDirectoryForTheme(thm) + File.separator + "style.css").toURI().toURL().toString();
			} catch (MalformedURLException e) {
				BaseProcedures.log("Failed to load theme stylesheet! Using default one...", ThemeLoader.class);
				BaseProcedures.log(BaseProcedures.stack2string(e), ThemeLoader.class);
				return "/style.css";
			}
		}
	}
	
	public static String getThemeName(String xmlString) {
		XMLParser parser = new XMLParser(xmlString);
		NodeList nl = parser.getDocumentRoot().getElementsByTagName("name");
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			return el.getTextContent();
		}
		return "unknown";
	}
	
	public static URL getFXMLFromTheme(Theme thm, String fxml) {
		if(thm == null) return ThemeLoader.class.getResource("/" + fxml + ".fxml");
		else {
			try {
				return new File(getDirectoryForTheme(thm) + File.separator + fxml + ".fxml").toURI().toURL();
			} catch (MalformedURLException e) {
				BaseProcedures.log("Failed to load FXML " + fxml + ".fxml from theme! Using default one...", ThemeLoader.class);
				BaseProcedures.log(BaseProcedures.stack2string(e), ThemeLoader.class);
				return ThemeLoader.class.getResource("/" + fxml + ".fxml");
			}
		}
	}

	public static Theme loadTheme(File file) {
		// TODO Auto-generated method stub
		return loadThemeFromXML(new File(file + File.separator + "theme.xml"));
	}

}
