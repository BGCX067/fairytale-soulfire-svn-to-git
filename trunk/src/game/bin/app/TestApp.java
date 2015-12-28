package game.bin.app;

import game.bin.effect.Effect_Blue_Fire;
import game.bin.effect.Effect_Fire;
import game.bin.effect.Effect_Rain;
import game.bin.gamesys.RuntimeCash;
import game.bin.obj.DynamicMesh;
import game.bin.obj.Mesh;
import game.bin.obj.StaticMesh;
import game.bin.terrain.Terrain;
import game.bin.view.CameraClass;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.system.DisplaySystem;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;
import com.jmex.subdivision.Vector;

public class TestApp extends Node{
	
	private PhysicsSpace physicsspace;
	
	public TestApp(String name){
		super(name);
		System.out.println("Test Class Created");
		physicsspace = RuntimeCash.getPhysicsSpace();
		runApp();
	}
	
	public void runApp(){
		
		/*Box sky = new Box("mesh", new Vector3f(), 10, 1, 10);
		sky.setLocalTranslation(new Vector3f(0,10,0));
		sky.setModelBound(new BoundingBox());
		sky.updateModelBound();
		this.attachChild(sky);
		this.attachChild(Effect_Rain.addRain(sky));*/
		
		
		Mesh gun = new Mesh("gun");
        gun.addModel("weapon/glock_17.3ds", "Glock 17");
        gun.addTexture("weapon/glock_17.png");
        gun.setLocalTranslation(0, 10, 0);
        this.attachChild(gun);
        this.attachChild(Effect_Blue_Fire.createFire(gun));
        //this.attachChild(Effect_Rain.addRain(gun));
        //gun.setLocalTranslation(new Vector3f(0,5,0));
         
        
        
        /*Mesh parabolic_knife = new Mesh("obj parabolic_knife");
        
        parabolic_knife.addModel("weapon/parabolic_knife.3DS", "mesh parabolic_knife");
        parabolic_knife.addTexture("weapon/parabolic_knife.png","model");
        //parabolic_knife.addTexture("weapon/glock_17.png");
        //gun.setLocalTranslation(0, 10, 0);
        //gun.setLocalTranslation(new Vector3f(0,5,0));
        this.attachChild(parabolic_knife);
        //parabolic_knife.printNodeChildren();*/
		
		/*
		Box floor = new Box("Floor", new Vector3f(), 10, 1, 10);
		floor.setModelBound(new BoundingBox());
		floor.updateModelBound();
		
        StaticPhysicsNode dynamic_node = physicsspace.createStaticNode();
        dynamic_node.setName("physics");
        dynamic_node.attachChild(floor);
        dynamic_node.generatePhysicsGeometry();
        dynamic_node.setMaterial(Material.WOOD);
        dynamic_node.getLocalTranslation().set(0, 0, 0);
        //this.attachChild(dynamic_node);
        
        Mesh gun = new Mesh("gun");
        gun.addModel("weapon/glock_17.3ds", "Glock 17");
        gun.addTexture("weapon/glock_17.png");
        //gun.setLocalTranslation(new Vector3f(0,5,0));
        //this.attachChild(gun);
        DynamicMesh gun_physic = new DynamicMesh("Glock", gun);
        gun_physic.setLocalTranslation(0, 5, 0);
        
        Terrain map = new Terrain("Map");
        map.homeGrownHeightMap();
        //this.attachChild(map);
        StaticMesh map_physic = new StaticMesh("Map", map);
        
        this.attachChild(gun_physic);
        //this.attachChild(map_physic);
        
        */
        Box floorC = new Box("floor", new Vector3f(), 20, 1, 20);
		floorC.setModelBound(new BoundingBox());
		floorC.updateModelBound();
		floorC.setLocalTranslation(new Vector3f(0,0,0));
        floorC.setDefaultColor(ColorRGBA.green.clone());
        
		StaticPhysicsNode floorp = physicsspace.createStaticNode();
        floorp.setName("physics");
        floorp.attachChild(floorC);
        floorp.getLocalTranslation().set(0, -10, 0);
        floorp.generatePhysicsGeometry();
        floorp.setMaterial(Material.IRON);
        
        //this.attachChild(floorp);
        
        /*
        DynamicPhysicsNode dice01;
        Box diceb01;
		
		diceb01 = new Box("dice", new Vector3f(), 1, 1, 1);
		diceb01.setModelBound(new BoundingBox());
		diceb01.updateModelBound();
		
		
        dice01 = physicsspace.createDynamicNode();
        dice01.setName("physics");
        dice01.attachChild(gun);
        dice01.generatePhysicsGeometry();
        dice01.setMaterial(Material.WOOD);
        //dice01.computeMass();
        dice01.setMass(100);
        dice01.getLocalTranslation().set(18, 10, -6);
        dice01.setAffectedByGravity(true);
        this.attachChild(dice01);*/
        
        System.out.println("Test Class Added");
	}
}
