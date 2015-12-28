package game.bin.view;

import com.jme.input.InputHandler;
import com.jme.input.NodeHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public class CameraClass extends Node{
	
	private DisplaySystem display;
	private Camera cam;
	private InputHandler input;
	
	public CameraClass(String name){
		display = DisplaySystem.getDisplaySystem();
	    cam = display.getRenderer().getCamera();
	    
	    setCamera("FreeCam");
	}
	
	public void setCamera(String camtyp){
		//input = GameClass.getInputHandler();

		CameraNode cameraNode = new CameraNode("Camera Node", cam);

		if(this != null){
	    	this.detachChildNamed("Camera Node");
	    }
		
		if(camtyp.equals("FixedCam")){
			
	        //cameraNode.setLocalTranslation(new Vector3f(0, 0, 0));
	        cameraNode.updateWorldData(0);
	        this.attachChild(cameraNode);
	        
	        GameClass.setInputHandler(null);
		}
		
		if(camtyp.equals("FreeCam")){
			
	        
	        cameraNode.setLocalTranslation(new Vector3f(0, 0, -100));
	        cameraNode.updateWorldData(0);
	        this.attachChild(cameraNode);
	        
	        GameClass.addInputHandler(new NodeHandler(cameraNode, 100f, 0.7f) );
		}
	}
}
