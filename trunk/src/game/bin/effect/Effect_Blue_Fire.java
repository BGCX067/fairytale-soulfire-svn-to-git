package game.bin.effect;

import game.bin.gamesys.ResourceLocatorLibaryTool;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class Effect_Blue_Fire {

	private static DisplaySystem display = DisplaySystem.getDisplaySystem();
	
	public static Node createFire(Node object){
		
		Node fireNode = new Node("fire");
		
		BlendState as1 = display.getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);
	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
	    
	    String texture_file_01 = "flame01.png";
	    String texture_file_02 = "flame02.png";
	    String texture_file_03 = "flame03.png";
	    
	    TextureState ts_01 = display.getRenderer().createTextureState();
	    ts_01.setTexture(TextureManager.loadTexture(
       		ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_EFFECT, texture_file_01),
	        Texture.MinificationFilter.Trilinear,
	        Texture.MagnificationFilter.Bilinear));
	    ts_01.setEnabled(true);
	    
	    TextureState ts_02 = display.getRenderer().createTextureState();
	    ts_02.setTexture(TextureManager.loadTexture(
       		ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_EFFECT, texture_file_02),
	        Texture.MinificationFilter.Trilinear,
	        Texture.MagnificationFilter.Bilinear));
	    ts_02.setEnabled(true);
	    
	    TextureState ts_03 = display.getRenderer().createTextureState();
	    ts_03.setTexture(TextureManager.loadTexture(
       		ResourceLocatorLibaryTool.locateResource(ResourceLocatorLibaryTool.TYPE_EFFECT, texture_file_03),
	        Texture.MinificationFilter.Trilinear,
	        Texture.MagnificationFilter.Bilinear));
	    ts_03.setEnabled(true);
	    
	    Node cn = (Node)object.getChild(0);

	    ParticleMesh manager01 = ParticleFactory.buildParticles("particles", 100);
	    manager01.setEmissionDirection(new Vector3f(0.0f, 0.5f, 0.0f));
	    manager01.setMaximumAngle(0.20943952f);
	    manager01.getParticleController().setSpeed(0.1f);
	    manager01.setMinimumLifeTime(70.0f);
	    manager01.setMaximumLifeTime(120.0f);
	    manager01.setStartSize(1.0f);
	    manager01.setEndSize(0.5f);
	    manager01.setStartColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager01.setEndColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 0.0f));
	    manager01.getParticleController().setControlFlow(false);
	    manager01.setInitialVelocity(0.12f); 
	    manager01.setGeometry((Geometry)(cn.getChild(0)));
	    manager01.warmUp(60);
	    manager01.setRenderState(ts_01);
	    manager01.setRenderState(as1);
	    manager01.setLightCombineMode(LightCombineMode.Off);
	    manager01.setTextureCombineMode(TextureCombineMode.Replace);
	    
	    ParticleMesh manager02 = ParticleFactory.buildParticles("particles", 100);
	    manager02.setEmissionDirection(new Vector3f(0.0f, 0.5f, 0.0f));
	    manager02.setMaximumAngle(0.20943952f);
	    manager02.getParticleController().setSpeed(0.1f);
	    manager02.setMinimumLifeTime(70.0f);
	    manager02.setMaximumLifeTime(120.0f);
	    manager02.setStartSize(1.0f);
	    manager02.setEndSize(0.5f);
	    manager02.setStartColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager02.setEndColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 0.0f));
	    manager02.getParticleController().setControlFlow(false);
	    manager02.setInitialVelocity(0.12f); 
	    manager02.setGeometry((Geometry)(cn.getChild(0)));
	    manager02.warmUp(60);
	    manager02.setRenderState(ts_02);
	    manager02.setRenderState(as1);
	    manager02.setLightCombineMode(LightCombineMode.Off);
	    manager02.setTextureCombineMode(TextureCombineMode.Replace);
	    
	    ParticleMesh manager03 = ParticleFactory.buildParticles("particles", 100);
	    manager03.setEmissionDirection(new Vector3f(0.0f, 0.5f, 0.0f));
	    manager03.setMaximumAngle(0.20943952f);
	    manager03.getParticleController().setSpeed(0.1f);
	    manager03.setMinimumLifeTime(100.0f);
	    manager03.setMaximumLifeTime(150.0f);
	    manager03.setStartSize(1.0f);
	    manager03.setEndSize(0.5f);
	    manager03.setStartColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager03.setEndColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 0.0f));
	    manager03.getParticleController().setControlFlow(false);
	    manager03.setInitialVelocity(0.12f); 
	    manager03.setGeometry((Geometry)(cn.getChild(0)));
	    manager03.warmUp(60);
	    manager03.setRenderState(ts_03);
	    manager03.setRenderState(as1);
	    manager03.setLightCombineMode(LightCombineMode.Off);
	    manager03.setTextureCombineMode(TextureCombineMode.Replace);
	    
	    ParticleMesh manager04 = ParticleFactory.buildParticles("particles", 100);
	    manager04.setEmissionDirection(new Vector3f(0.0f, 0.1f, 0.0f));
	    manager04.setMaximumAngle(0.20943952f);
	    manager04.getParticleController().setSpeed(0.1f);
	    manager04.setMinimumLifeTime(10.0f);
	    manager04.setMaximumLifeTime(20.0f);
	    manager04.setStartSize(1.0f);
	    manager04.setEndSize(0.5f);
	    manager04.setStartColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 1.0f));
	    manager04.setEndColor(new ColorRGBA(0.121f, 0.312f, 1.0f, 0.0f));
	    manager04.getParticleController().setControlFlow(false);
	    manager04.setInitialVelocity(0.12f); 
	    manager04.setGeometry((Geometry)(cn.getChild(0)));
	    manager04.warmUp(60);
	    manager04.setRenderState(ts_03);
	    manager04.setRenderState(as1);
	    manager04.setLightCombineMode(LightCombineMode.Off);
	    manager04.setTextureCombineMode(TextureCombineMode.Replace);
	    
	    ZBufferState zstate = display.getRenderer().createZBufferState();
	    zstate.setWritable(false);
	    //zstate.setEnabled(false);
	    manager01.setRenderState(zstate);
	    manager02.setRenderState(zstate);
	    manager03.setRenderState(zstate);
	    manager04.setRenderState(zstate);
	    
	    
	    
	    
	    fireNode.attachChild(manager01);
	    fireNode.attachChild(manager02);
	    fireNode.attachChild(manager03);
	    fireNode.attachChild(manager04);
	    
	    return fireNode;
	}
}