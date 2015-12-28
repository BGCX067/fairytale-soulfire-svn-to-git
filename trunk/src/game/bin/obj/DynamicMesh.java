package game.bin.obj;

import game.bin.gamesys.RuntimeCash;

import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;

public class DynamicMesh extends Node{
	
	
	private DynamicPhysicsNode dynamicphysicsnode;
	private PhysicsSpace physicsspace;
	
	private int mass = 1;
	
	public DynamicMesh(String name, Node object){
		super(name);
		physicsspace = RuntimeCash.getPhysicsSpace();
		
		dynamicphysicsnode = physicsspace.createDynamicNode();
		dynamicphysicsnode.setName("physics");
		dynamicphysicsnode.attachChild(object);
		dynamicphysicsnode.generatePhysicsGeometry(true);
		dynamicphysicsnode.setMaterial(Material.IRON);
		//dynamicphysicsnode.computeMass();
		dynamicphysicsnode.setMass(100);
		//dynamicphysicsnode.generatePhysicsGeometry(true);
		
		dynamicphysicsnode.setAffectedByGravity(true);
		this.attachChild(dynamicphysicsnode);
	}
	
	public void setLocalTranslation(float x, float z, float y){
		this.getLocalTranslation().set(x, z, y);
	}
}
