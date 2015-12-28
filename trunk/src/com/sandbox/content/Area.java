package com.sandbox.content;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.terrain.TerrainPage;
import com.sandbox.util.Converter;
import com.sandbox.util.FileClass;

public class Area {
	
	private boolean update = false;
	private String name;
	private int size;
	private boolean type;
	
	private float[] map;
	
	private Node area = new Node("Area");
	TerrainPage page;
	
	public Area(String name, int size, boolean type){
		this.name = name;
		this.size = size+1;
		this.type = type;
		
		
		if(size!=0){
			createMap();
			recreateArea();
		}
		
		update = true;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean getType(){
		return type;
	}
	
	public void reloadSize(){
		size = (int)Math.sqrt(map.length);
	}
	
	public int getSize(){
		return size;
	}
	
	
	public void createMap(){
		float[] map = new float[size*size];
		if(size != 0){
			for(int i = 0; i < size; i++){
				map[i] = 0;
			}
			map[0] = 0;
		}
		this.map = map;
	}
	
	public void loadMap(){
		FileClass areafile = new FileClass("src/com/sandbox/data/map/TestMap.mp");
		name = areafile.search("mapname");
		map = Converter.Array_StringToFloat(areafile.search("height"));
		
		System.out.println("Load");
		recreateArea();
	}
	
	public float[] getMap(){
		return map;
	}
	
	public void recreateArea(){
		
		//
		reloadSize();
		if(size!=0){
			
			area.detachAllChildren();
			/*TerrainPage(String name, int blockSize, int size,
		            Vector3f stepScale, float[] heightMap)*/
	        // Create a terrain block.  Our integer height values will scale on the map 2x larger x,
	        //   and 2x larger z.  Our map's origin will be the regular origin.
	        TerrainPage tb = new TerrainPage("Terrain", 10, getSize(), new Vector3f(1,1,1),getMap());
	
	        // Give the terrain a bounding box.
	        tb.setModelBound(new BoundingBox());
	        tb.updateModelBound();
	        
	        page = tb;
	        area.attachChild(tb);
	        
	        update = true;
		}
	}
	
	public TerrainPage getTerrain(){
		return page;
	}
	
	public void addArea(Node attachment){
		this.area.attachChild(attachment);
		update = true;
	}
	
	public void setArea(Node area){
		this.area = area;
		update = true;
	}
	
	public Node getArea(){
		return this.area;
	}
	
	public boolean updateArea(){
		return update;
	}
	
	public void isUpdated(){
		update = false;
	}
}
