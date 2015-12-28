package game.bin;

import game.bin.app.PokerDiceGame;
import game.bin.app.TestApp;
import game.bin.view.CameraClass;

import com.jme.scene.Node;

public class GameApp extends Node{

	public GameApp(String name){
		super(name);
		runApp();
	}
	
	public void runApp(){
		
		//This add the camera view
		//this.attachChild(new CameraClass("Camera"));
		
		//this.attachChild(new PokerDiceGame("PokerDiceGame"));
		this.attachChild(new TestApp("TestApp"));
		
	}
}
