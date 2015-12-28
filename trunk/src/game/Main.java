package game;


import game.bin.GameApp;
import game.bin.gamesys.EngineGameState;
import game.bin.gamesys.PhysicsGame;
import game.bin.gamesys.ResourceLocatorAdvanced;
import game.bin.gamesys.ResourceLocatorLibaryTool;
import game.bin.gamesys.RuntimeCash;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jmex.game.state.GameStateManager;

/**
 * The Entry point.
 * Creates a game instance, points the resource locator to the resources
 * and creates / activated the GameStates.
 *  
 * @author Christoph Luder
 */
public class Main {
	
	private static PhysicsGame game;
	private static EngineGameState gamestate;
	
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// only show Warnings
	    System.setProperty("jme.stats", "set");
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		Logger.getLogger("com.jmex").setLevel(Level.WARNING);

				
		// set up resource search paths
		try {
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_AUDIO,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/audio/")));
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_SOUND,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/sound/")));
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_TEXTURE,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/texture/")));
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_MODEL,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/model/")));
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_SHADER,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/shader/")));
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_PARTICLE,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/particle/")));
			ResourceLocatorLibaryTool.addResourceLocator(ResourceLocatorLibaryTool.TYPE_EFFECT,
					new ResourceLocatorAdvanced(Main.class.getClassLoader().getResource("game/data/effect/")));
		} catch (URISyntaxException e1) {
			PhysicsGame.get().getGame().finish();
		}
		
		// create a StandardGame instance, set some settings and start it up 
		game = PhysicsGame.get();
		game.getGame().getSettings().setFullscreen(false);
		game.getGame().getSettings().setWidth(800);
		game.getGame().getSettings().setHeight(600);
		game.getGame().getSettings().setFramerate(60);
		game.getGame().getSettings().setVerticalSync(true);
		game.getGame().getSettings().setStencilBits(4);
		game.getGame().getSettings().setDepthBits(24);
		game.getGame().getSettings().setSamples(4);
		game.getGame().getSettings().setSFX(true);
		game.getGame().getSettings().setMusic(false);
		game.getGame().start();
		

		// create and activate the GameStates
		
		gamestate = new EngineGameState("Engine");
		GameStateManager.getInstance().attachChild(gamestate);
		gamestate.setActive(true);
		
		RuntimeCash.setPhysicsSpace(gamestate.getPhysicsSpace());
		
		gamestate.getRootNode().attachChild(new GameApp("GameApp"));
		gamestate.getRootNode().updateGeometricState(0, true);
        gamestate.getRootNode().updateRenderState();
		
		GameStateManager.getInstance().activateAllChildren();
	}
}
