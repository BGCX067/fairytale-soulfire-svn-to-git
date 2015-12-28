package game.bin.app;

import game.bin.gamesys.RuntimeCash;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

public class PokerDiceGame extends Node{
	
	private PhysicsSpace physicsspace;
	
	public PokerDiceGame(String name){
		super(name);
		physicsspace = RuntimeCash.getPhysicsSpace();
		
		runApp();
	}
	
	public void runApp(){
		createBox();
		createDices();
	}
	
	public void createDices(){
		DynamicPhysicsNode dice01,dice02,dice03,dice04,dice05;
		Box diceb01,diceb02,diceb03,diceb04,diceb05;
		
		diceb01 = new Box("dice", new Vector3f(), 1, 1, 1);
		diceb01.setModelBound(new BoundingBox());
		diceb01.updateModelBound();
		
		diceb02 = new Box("dice", new Vector3f(), 1, 1, 1);
		diceb02.setModelBound(new BoundingBox());
		diceb02.updateModelBound();
		
		diceb03 = new Box("dice", new Vector3f(), 1, 1, 1);
		diceb03.setModelBound(new BoundingBox());
		diceb03.updateModelBound();
		
		diceb04 = new Box("dice", new Vector3f(), 1, 1, 1);
		diceb04.setModelBound(new BoundingBox());
		diceb04.updateModelBound();
		
		diceb05 = new Box("dice", new Vector3f(), 1, 1, 1);
		diceb05.setModelBound(new BoundingBox());
		diceb05.updateModelBound();
		//dice.setLocalTranslation(new Vector3f(0,10,0));
		
		
        dice01 = physicsspace.createDynamicNode();
        dice01.setName("physics");
        dice01.attachChild(diceb01);
        dice01.generatePhysicsGeometry();
        dice01.setMaterial(Material.WOOD);
        dice01.computeMass();
        dice01.setMass(100);
        dice01.getLocalTranslation().set(18, 10, -6);
        dice01.setAffectedByGravity(true);
        dice01.addForce(new Vector3f(-100000,-60000,0));
		
        
        
        
        
        this.attachChild(dice01);
        
        dice02 = physicsspace.createDynamicNode();
        dice02.setName("physics");
        dice02.attachChild(diceb02);
        dice02.generatePhysicsGeometry();
        dice02.setMaterial(Material.IRON);
        dice02.computeMass();
        dice02.setMass(640);
        dice02.getLocalTranslation().set(18, 20, -3);
        dice02.setAffectedByGravity(true);
        
        this.attachChild(dice02);
        
        dice03 = physicsspace.createDynamicNode();
        dice03.setName("physics");
        dice03.attachChild(diceb03);
        dice03.generatePhysicsGeometry();
        dice03.setMaterial(Material.IRON);
        dice03.computeMass();
        dice03.setMass(640);
        dice03.getLocalTranslation().set(18, 20, 0);
        dice03.setAffectedByGravity(true);
        
        this.attachChild(dice03);
        
        dice04 = physicsspace.createDynamicNode();
        dice04.setName("physics");
        dice04.attachChild(diceb04);
        dice04.generatePhysicsGeometry();
        dice04.setMaterial(Material.IRON);
        dice04.computeMass();
        dice04.setMass(640);
        dice04.getLocalTranslation().set(18, 20, 3);
        dice04.setAffectedByGravity(true);
        
        this.attachChild(dice04);
        
        dice05 = physicsspace.createDynamicNode();
        dice05.setName("physics");
        dice05.attachChild(diceb05);
        dice05.generatePhysicsGeometry();
        dice05.setMaterial(Material.IRON);
        dice05.computeMass();
        dice05.setMass(640);
        dice05.getLocalTranslation().set(18, 20, 6);
        dice05.setAffectedByGravity(true);
        
        this.attachChild(dice05);
        
	}
	
	public void createBox(){
		Box floor = new Box("floor", new Vector3f(), 20, 1, 12);
		floor.setModelBound(new BoundingBox());
		floor.updateModelBound();
		floor.setLocalTranslation(new Vector3f(0,0,0));
        floor.setDefaultColor(ColorRGBA.green.clone());
        
		StaticPhysicsNode floorp = physicsspace.createStaticNode();
        floorp.setName("physics");
        floorp.attachChild(floor);
        floorp.generatePhysicsGeometry();
        floorp.setMaterial(Material.IRON);
        
        this.attachChild(floorp);
        
        //
        Box wall01, wall02, wall03, wall04;
        StaticPhysicsNode wallN, wallW, wallS, wallE;
        
        wall01 = new Box("wallN", new Vector3f(), 20, 3, 1);
        wall01.setModelBound(new BoundingBox());
        wall01.updateModelBound();
        wall01.setLocalTranslation(new Vector3f(0,2,13));
        wall01.setDefaultColor(ColorRGBA.red.clone());
        
		wallN = physicsspace.createStaticNode();
		wallN.setName("physics");
		wallN.attachChild(wall01);
		wallN.generatePhysicsGeometry();
		wallN.setMaterial(Material.IRON);
        
        this.attachChild(wallN);
        
        wall02 = new Box("wallS", new Vector3f(), 20, 3, 1);
        wall02.setModelBound(new BoundingBox());
        wall02.updateModelBound();
        wall02.setLocalTranslation(new Vector3f(0,2,-13));
        wall02.setDefaultColor(ColorRGBA.red.clone());
        
		wallS = physicsspace.createStaticNode();
		wallS.setName("physics");
		wallS.attachChild(wall02);
		wallS.generatePhysicsGeometry();
		wallS.setMaterial(Material.IRON);
        
        this.attachChild(wallS);
        
        wall03 = new Box("wallW", new Vector3f(), 1, 3, 14);
        wall03.setModelBound(new BoundingBox());
        wall03.updateModelBound();
        wall03.setLocalTranslation(new Vector3f(-21,2,0));
        wall03.setDefaultColor(ColorRGBA.red.clone());
        
		wallW = physicsspace.createStaticNode();
		wallW.setName("physics");
		wallW.attachChild(wall03);
		wallW.generatePhysicsGeometry();
		wallW.setMaterial(Material.IRON);
        
        this.attachChild(wallW);
        
        wall04 = new Box("wallW", new Vector3f(), 1, 3, 14);
        wall04.setModelBound(new BoundingBox());
        wall04.updateModelBound();
        wall04.setLocalTranslation(new Vector3f(21,2,0));
        wall04.setDefaultColor(ColorRGBA.red.clone());
        
		wallE = physicsspace.createStaticNode();
		wallE.setName("physics");
		wallE.attachChild(wall04);
		wallE.generatePhysicsGeometry();
		wallE.setMaterial(Material.IRON);
        
        this.attachChild(wallE);
	}
}
