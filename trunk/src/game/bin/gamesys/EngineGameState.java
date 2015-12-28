package game.bin.gamesys;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.CullState.Face;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.geom.Debugger;
import com.jmedemos.physics_fun.physics.PhysicsWindCallback;
import com.jmedemos.physics_fun.util.SceneSettings;
import com.jmex.effects.glsl.BloomRenderPass;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.util.PhysicsPicker;
import com.jmex.physics.util.states.PhysicsGameState;

/**
 * The main GameState.
 * Creates the Physics Playground.
 * 
 * @author Christoph Luder
 */
public class EngineGameState extends PhysicsGameState {
    /** reference to the camera */
	private Camera cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
	/** we want to move the camera first person style */
	private FirstPersonHandler movementInput = null;
	/** InputHandler for the physics picker and basic command */
	private InputHandler input = new InputHandler();
	/** the static floor */
	private StaticPhysicsNode floor = null;
	/** The Node where newly created objects are attached to. */
	private Node objectsNode = null;
	
	/** should the physics debug view be rendereed */ 
	private boolean showPhysics = false;
	/** should the bounding boxes be rendered */
	private boolean showBounds = false;

	/** the physics picker */
	private PhysicsPicker picker = null;
	/** The physics Wind. */
	private PhysicsWindCallback wind = null;
	
	/** Display */
	private DisplaySystem display = DisplaySystem.getDisplaySystem();
	/** GLSLShader */
	private GLSLShaderObjectsState so_normalmap;
	/** BloomShader */
	private RenderPass rootPass = new RenderPass();
	private BasicPassManager pManager = new BasicPassManager();
	private BloomRenderPass bloomRenderPass;
	
	
	/** Logger */
	private static final Logger logger = Logger.getLogger(EngineGameState.class.getName());
	
	/**
	 * Constructs the MainGameState.
	 * Creates the scene and add the different objects to the Scenegraph.
	 * 
	 * @param name name of the GameState
	 */
	
	
	public EngineGameState(String name) {
		super(name);
		
		//display.setVSyncEnabled(true);
		
		//create Bloom
		rootPass.add(rootNode);
		pManager.add(rootPass);
		
		// create the scene
        picker = new PhysicsPicker( input, rootNode, getPhysicsSpace(), true);
        picker.getInputHandler().setEnabled(false);
        
        try {
			GameTaskQueueManager.getManager().update(new Callable<Object>() {
				public Object call() throws Exception {
					bloomRenderPass = new BloomRenderPass(cam, 4);
					bloomRenderPass.setExposurePow(3);
					
					display.setVSyncEnabled(RuntimeCash.getVSyncro());
					display.setMinSamples(RuntimeCash.getAntiAliasing());
					
					return null;
				}
			}).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	    if(!bloomRenderPass.isSupported()) {
	    	System.err.println("Bloom not Supported");
	    	System.exit(0);
	    } else {
	        bloomRenderPass.add(rootNode);
	        bloomRenderPass.setUseCurrentScene(true);
	        pManager.add(bloomRenderPass);
	    }
        
        init();
        setupInput();
        setupLight();
	}
	
	
	/**
	 * Initializes the rootNodes RenderStates, initializes the Camera and 
	 */
	private void init() {
	    
	    // create a first person controller to move the Camera with W,A,S,D and mouse look
	    movementInput = new FirstPersonHandler(cam, 15.0f, 0.5f);
	    // move the camera a bit backwards and up
	    cam.setLocation(new Vector3f(2, 10, 15));
	    
	    // create a Physics update callback, to simulate basic wind force
	    wind = new PhysicsWindCallback(SceneSettings.get().getWindVariation(),
	                                    SceneSettings.get().getWindForce());
	    getPhysicsSpace().addToUpdateCallbacks(wind);
	    
	    // Normal Mapping Shader
	    if(RuntimeCash.getNormalMapping()){
	    	so_normalmap = display.getRenderer().createGLSLShaderObjectsState();

	        // Check is GLSL is supported on current hardware.
	        if (!GLSLShaderObjectsState.isSupported()) {
	            logger.severe("Your graphics card does not support GLSL programs, and thus cannot run Normal Mapping.");
	            RuntimeCash.setNormalMapping(false);
	        }
	    	
	    	reloadShader();
	    }
	    
	    // Bloom Shader
	    CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
	    cs.setCullFace(Face.None);
	    //cs.setCullFace(Face.Back);
	    cs.setEnabled(true);
	    rootNode.setRenderState(cs);
		
	}
	
	/**
	 * create some light sources to illuminate the scene.
	 */
	private void setupLight() {
		LightState ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();

		DirectionalLight dr1 = new DirectionalLight();
        dr1.setEnabled(true);
        dr1.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
        dr1.setDiffuse(ColorRGBA.white.clone());
        dr1.setDirection(new Vector3f(-0.2f, -0.3f, -0.4f).normalizeLocal());
        dr1.setShadowCaster(true);

        ls.attach(dr1);
		ls.setEnabled(true);
		ls.setGlobalAmbient(new ColorRGBA(0.6f, 0.6f, 0.6f, 1.0f));
		ls.setTwoSidedLighting(false);

		rootNode.setRenderState(ls);
	}
	
	public void reloadShader() {
		
		/*GLSLShaderObjectsState testShader = DisplaySystem.getDisplaySystem()
                .getRenderer().createGLSLShaderObjectsState();
        try {
        	testShader.load(
        			ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_SHADER, "normalmap.vert"), 
        			ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_SHADER, "normalmap.frag")
        			);
        	
            testShader.apply();
            DisplaySystem.getDisplaySystem().getRenderer().checkCardError();
        } catch (JmeException e) {
        	RuntimeCash.setNormalMapping(false);
            logger.log(Level.WARNING, "Failed to reload shader", e);
            return;
        }*/
        
        so_normalmap.load(
    			ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_SHADER, "normalmap.vert"), 
    			ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_SHADER, "normalmap.frag")
    			);
        so_normalmap.setUniform("baseMap", 0);
        so_normalmap.setUniform("normalMap", 1);
        so_normalmap.setUniform("specularMap", 2);
        
        logger.info("Shader reloaded...");
        
        RuntimeCash.setGLSLShaderObjectNormalMap(so_normalmap);
    }
	
	/**
	 * set up some key actions.
	 * - SPACE to release a new object,
	 * - ESC to quit the game
	 * - TAB to enable / disable the GUI GameState
	 */
	private void setupInput() {
		input.addAction( new InputAction() {
			public void performAction( InputActionEvent evt ) {
				if ( evt.getTriggerPressed() ) {
					PhysicsGame.get().getGame().finish();
					System.exit(0);
				}
			}
		}, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_ESCAPE, InputHandler.AXIS_NONE, false );
		
		input.addAction( new InputAction() {
		    public void performAction( InputActionEvent evt ) {
		        if ( evt.getTriggerPressed() ) {
		        	System.out.println("Spawn Object");
		        }
		    }
		}, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false );
		
		input.addAction( new InputAction() {
		    public void performAction( InputActionEvent evt ) {
		        if ( evt.getTriggerPressed() ) {
		        	System.out.println("Get Tab");
		        }
		    }
		}, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_TAB, InputHandler.AXIS_NONE, false );
	}

	/**
	 * we update the input controllers and some physic object if needed,
	 * then we update the physics world and call updateGeometricstate()
	 * which happens in super.update().
	 */
	@Override
	public void update(float tpf) {
		input.update(tpf);
		
		pManager.updatePasses(tpf);
//		swing.update();
		
		/*if(RuntimeCash.getNormalMapping()){
	    	reloadShader();
	    }*/
		
		if (movementInput.isEnabled()) {
			movementInput.update(tpf);
		}
		
		rootNode.updateGeometricState(tpf, true);
		super.update(tpf);
		
	}
	
	public void cleanup() {
		super.cleanup();
		
		bloomRenderPass.cleanup();
	}
	
	/**
	 * render the scene, draw bounds or physics if needed.
	 */
	@Override
	public void render(float tpf) {
	    super.render(tpf);
	    if (showPhysics) {
	        PhysicsDebugger.drawPhysics(getPhysicsSpace(),
	                DisplaySystem.getDisplaySystem().getRenderer());
	    }
	    
	    if (showBounds) {
	        Debugger.drawBounds(getRootNode(),
	                DisplaySystem.getDisplaySystem().getRenderer());
	    }
	    
	    pManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
	}

	public boolean isShowPhysics() {
		return showPhysics;
	}

	public void setShowPhysics(boolean showPhysics) {
		this.showPhysics = showPhysics;
	}

    public PhysicsPicker getPicker() {
        return picker;
    }

    public PhysicsWindCallback getWind() {
        return wind;
    }

    public void setShowBounds(boolean showBounds) {
        this.showBounds = showBounds;
    }
    
    public void quit() {
    	if (display != null){
            display.close();
    	}
        System.exit( 0 );
    }
}
