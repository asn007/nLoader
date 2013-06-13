package eu.q_b.asn007.nloader.theming;

import java.io.File;
import java.util.LinkedList;

import javafx.scene.image.Image;

import eu.q_b.asn007.nloader.BaseProcedures;

public class Theme {

	public String THEME_NAME;
	public String THEME_AUTHOR;
	public String THEME_VERSION;
	
	public int SCENE_WIDTH;
	public int SCENE_HEIGHT;
	
	public File THEME_DIRECTORY;
	public LinkedList<File> THEME_FILES;
	
	public Image THEME_ICON;
	
	public Theme(String name, String author, String version, int width, int height, Image icon) {
		this.THEME_NAME = name;
		this.THEME_AUTHOR = author;
		this.THEME_VERSION = version;
		this.SCENE_HEIGHT = height;
		this.SCENE_WIDTH = width;
		this.THEME_ICON = icon;
		this.THEME_DIRECTORY = ThemeLoader.getDirectoryForTheme(this);
		THEME_FILES = BaseProcedures.addFiles(THEME_DIRECTORY);
	}
	
}
