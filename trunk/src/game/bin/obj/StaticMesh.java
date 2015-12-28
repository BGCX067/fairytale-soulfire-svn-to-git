package game.bin.obj;

import game.bin.gamesys.RuntimeCash;

import com.jme.scene.Node;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

public class StaticMesh extends Node{

	private StaticPhysicsNode staticphysicsnode;
	private PhysicsSpace physicsspace;
	
	private int mass = 1;
	
	public StaticMesh(String name, Node model){
		super(name);
		physicsspace = RuntimeCash.getPhysicsSpace();
		
		staticphysicsnode = physicsspace.createStaticNode();
		staticphysicsnode.setName("physics");
		staticphysicsnode.attachChild(model);
		staticphysicsnode.generatePhysicsGeometry();
		staticphysicsnode.setMaterial(Material.IRON);
		this.attachChild(staticphysicsnode);
	}
	
	public void setLocalTranslation(float x, float z, float y){
		this.getLocalTranslation().set(x, z, y);
	}
}
