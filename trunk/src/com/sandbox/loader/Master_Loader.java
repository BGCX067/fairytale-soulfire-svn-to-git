package game.bin.loader;

public class Master_Loader {
	
	public static void main(String[] args) {
		Master_Loader boot = new Master_Loader();
	}
	
	public Master_Loader(){
		//
		loadMap();
	}
	
	public String loadMap(){
		FileUtil tool = new FileUtil("src/game/data/modul/game.ini");
		FileUtil map = new FileUtil("src/game/data/maps/TestMap.terr");;
		int[] test = map.convertArrayString(map.searchFromFile("height2"));
		
		return null;
	}
}
