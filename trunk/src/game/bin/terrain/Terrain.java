package game.bin.terrain;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.TerrainPage;

public class Terrain extends Node{

	public Terrain(String name){
		super(name);
	}
	
	public void homeGrownHeightMap() {
        // The map for our terrain.  Each value is a height on the terrain
        float[] map=new float[]{
            1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,4,
            2,1,2,3,1,2,3,4,1,2,3,4,1,2,3,4,4,
            3,2,1,2,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,4,
            2,1,2,3,1,2,3,4,1,2,3,4,1,2,3,4,4,
            3,2,1,2,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,4,
            2,1,2,3,1,2,3,4,1,2,3,4,1,2,3,4,4,
            3,2,1,2,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4,
            4,3,2,1,1,2,3,4,1,2,3,4,1,2,3,4,4
        };
        
        TerrainPage tb = new TerrainPage("Terrain", 10, 17, new Vector3f(5,1,5),map);
    	
        // Give the terrain a bounding box.
        tb.setModelBound(new BoundingBox());
        tb.updateModelBound();

        // Attach the terrain TriMesh to our rootNode
        this.attachChild(tb);
    }
}
