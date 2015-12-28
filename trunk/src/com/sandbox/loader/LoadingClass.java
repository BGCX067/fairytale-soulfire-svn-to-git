package com.sandbox.loader;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

public class LoadingClass {
private URL file_path;
	
	public Hashtable filetable = new Hashtable();
	
	public Hashtable meshtable = new Hashtable();
	public Hashtable texturetable = new Hashtable();
	public Hashtable groundtexturetable = new Hashtable();
	
	
	public boolean root_path = false;
	
	public String SANDBOX_PATH = "./scr/com/sandbox";
	public String GAME_PATH = "./scr/game";
	
	public String FILE_PATH_MESH = "data/mesh";
	public String FILE_PATH_TEXTURE = "data/texture";
	public String FILE_PATH_GROUND_TEXTURE = "data/ground";
	
	
	public String USER_ID = "0001";
	
	public static void main(String[] args) {
		LoadingClass loader = new LoadingClass();
	}
	
	
	public LoadingClass(){
		//file_path = FileLoader.class.getClassLoader().getResource("game/data");
		
		//searchFiles("./src/game/data");
		searchFiles("./src/game/data/mesh", filetable);
		showElements(filetable);
	}
	
	public void searchFiles(String path, Hashtable table){
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				addElement(listOfFiles[i].getName(), listOfFiles[i].getPath(), table);
			} else if (listOfFiles[i].isDirectory()) {
				searchFiles(path + "/" + listOfFiles[i].getName(), table);
			}
	    }
	}
	
	public void addElement(String key, String path, Hashtable table){
		table.put(key, path);
	}
	
	
	
	
	public void showElements(Hashtable table){
		//Ausgabe
		Enumeration e = table.keys();
		
		while (e.hasMoreElements()) {
			String alias = (String)e.nextElement();
			String type	= alias.substring(alias.lastIndexOf(".") + 1, alias.length());
			
			if (type.equals("md5anim")){
                type = "MD5";
                System.out.println(type + " - " + alias + " - " + table.get(alias));
			}
			
			if (type.equals("3ds")||type.equals("3DS")){
                type = "3DS";
                System.out.println(type + " - " + alias + " - " + table.get(alias));
			}
			
			else{
				System.out.println(
					alias + " --> " + table.get(alias)
				);
			}
		}
	}
}
