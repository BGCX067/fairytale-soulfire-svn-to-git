package game.bin.effect;

import game.bin.gamesys.ResourceLocatorLibaryTool;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class Effect_Rain {

private static DisplaySystem display = DisplaySystem.getDisplaySystem();
	
	public static Node addRain(Spatial object){
		Node rainNode = new Node("effect_rain");
		rainNode.attachChild(addRainEmitter("rain/rain.png", object, 1000));
		return rainNode;
	}
	
	public static Node addRain(Node object){
		Node rainNode = new Node("effect_rain");
		rainNode.attachChild(addRainEmitter("rain/rain.png", object, 100));
		return rainNode;
	}
	
	public static Node addRainEmitter(String texture_file, Spatial object, int particle){
		
		Node rainNode = new Node("rain_emitter");
	
		BlendState as1 = display.getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);
	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
		
	    TextureState ts = display.getRenderer().createTextureState();
	    ts.setTexture(TextureManager.loadTexture(
	   		ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_EFFECT, texture_file),
	        Texture.MinificationFilter.Trilinear,
	        Texture.MagnificationFilter.Bilinear));
	    ts.setEnabled(true);
	    
	    
	    ParticleMesh manager = ParticleFactory.buildParticles("particles", particle);
	    manager.setEmissionDirection(new Vector3f(0.0f, -5.0f, 0.0f));
	    manager.setMaximumAngle(0.20943952f);
	    manager.getParticleController().setSpeed(0.1f);
	    manager.setMinimumLifeTime(300.0f);
	    manager.setMaximumLifeTime(400.0f);
	    manager.setStartSize(1.0f);
	    manager.setEndSize(1.0f);
	    manager.setStartColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager.setEndColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager.getParticleController().setControlFlow(false);
	    manager.setInitialVelocity(0.12f); 
	    
	    
	    manager.setGeometry((Geometry) object);
	    manager.warmUp(60);
	    manager.setRenderState(ts);
	    manager.setRenderState(as1);
	    manager.setLightCombineMode(LightCombineMode.Off);
	    manager.setTextureCombineMode(TextureCombineMode.Replace);
	    
	    
	    ZBufferState zstate = display.getRenderer().createZBufferState();
	    zstate.setWritable(false);
	    //zstate.setEnabled(false);
	    manager.setRenderState(zstate);
	    
	    rainNode.attachChild(manager);
	    return rainNode;
	}
	
	public static Node addRainEmitter(String texture_file, Node object, int particle){
		
		Node rainNode = new Node("rain_emitter");

		BlendState as1 = display.getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);
	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
		
	    TextureState ts = display.getRenderer().createTextureState();
	    ts.setTexture(TextureManager.loadTexture(
       		ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_EFFECT, texture_file),
	        Texture.MinificationFilter.Trilinear,
	        Texture.MagnificationFilter.Bilinear));
	    ts.setEnabled(true);
	    
	    Node cn = (Node)object.getChild(0);
	    
	    ParticleMesh manager = ParticleFactory.buildParticles("particles", particle);
	    manager.setEmissionDirection(new Vector3f(0.0f, -1.0f, 0.0f));
	    manager.setMaximumAngle(0.20943952f);
	    manager.getParticleController().setSpeed(0.1f);
	    manager.setMinimumLifeTime(300.0f);
	    manager.setMaximumLifeTime(400.0f);
	    manager.setStartSize(0.2f);
	    manager.setEndSize(0.2f);
	    manager.setStartColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager.setEndColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 0.0f));
	    manager.getParticleController().setControlFlow(false);
	    manager.setInitialVelocity(0.12f); 
	    
	    
	    manager.setGeometry((Geometry)(cn.getChild(0)));
	    manager.warmUp(60);
	    manager.setRenderState(ts);
	    manager.setRenderState(as1);
	    manager.setLightCombineMode(LightCombineMode.Off);
	    manager.setTextureCombineMode(TextureCombineMode.Replace);
	    
	    
	    ZBufferState zstate = display.getRenderer().createZBufferState();
	    zstate.setWritable(false);
	    //zstate.setEnabled(false);
	    manager.setRenderState(zstate);
	    
	    rainNode.attachChild(manager);
	    return rainNode;
	}
}
