package game.bin.loader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import com.jmex.model.converters.MaxToJme;

public class FileLoader {
	
	private URL file_path;
	
	private Hashtable filetable = new Hashtable();
	
	public static void main(String[] args) {
		FileLoader loader = new FileLoader();
	}
	
	public FileLoader(){
		file_path = FileLoader.class.getClassLoader().getResource("game/data");
		
		//searchFiles("./src/game/data");
		searchFiles("./src/game/data/modul");
		showElements();
	}
	
	public void searchFiles(String path){
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				addElement(listOfFiles[i].getName(), listOfFiles[i].getPath());
			} else if (listOfFiles[i].isDirectory()) {
				searchFiles(path + "/" + listOfFiles[i].getName());
			}
	    }
	}
	
	public void addElement(String key, String path){
		filetable.put(key, path);
	}
	
	public void showElements(){
		//Ausgabe
		Enumeration e = filetable.keys();
		
		while (e.hasMoreElements()) {
			String alias = (String)e.nextElement();
			String type	= alias.substring(alias.lastIndexOf(".") + 1, alias.length());
			
			if (type.equals("3ds")){
                type = "Model";
                
                System.out.println(
    				alias + " --> " + filetable.get(alias) + " --> " + type
    			);
			}
			else{
				System.out.println(
					alias + " --> " + filetable.get(alias)
				);
			}
		}
	}
}
