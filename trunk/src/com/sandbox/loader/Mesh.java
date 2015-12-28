package game.bin.loader;

import game.bin.effects.Effect_Fire;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.model.animation.JointController;
import com.jmex.model.converters.AseToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;
import com.jmex.model.util.ModelLoader;

public class Mesh extends Node{
	
	public String textureToLoad = "";
	public Node loadedModel;
	DisplaySystem display = DisplaySystem.getDisplaySystem();
	
	public Mesh (String modelFile){
        loadedModel = null;
        FormatConverter	formatConverter = null;		
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        String modelFormat = modelFile.substring(modelFile.lastIndexOf(".") + 1, modelFile.length());
        String modelBinary = modelFile.substring(0, modelFile.lastIndexOf(".") + 1) + "jbin";
        URL modelURL = Mesh.class.getClassLoader().getResource(modelBinary);
        
        

        //verify the presence of the jbin model
        if (modelURL == null){

                modelURL = ModelLoader.class.getClassLoader().getResource(modelFile);

                //evaluate the format
                if (modelFormat.equals("3ds")){
                        formatConverter = new MaxToJme();
                } else if (modelFormat.equals("md2")){
                        formatConverter = new Md2ToJme();
                } else if (modelFormat.equals("md3")){
                        formatConverter = new Md3ToJme();
                } else if (modelFormat.equals("ms3d")){
                        formatConverter = new MilkToJme();
                } else if (modelFormat.equals("ase")){
                        formatConverter = new AseToJme();
                } else if (modelFormat.equals("obj")){
                        formatConverter = new ObjToJme();
                }
                formatConverter.setProperty("mtllib", modelURL);

                try {
                        formatConverter.convert(modelURL.openStream(), BO);
                        loadedModel = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));

                        //save the jbin format
                        BinaryExporter.getInstance().save((Savable)loadedModel, new File(modelBinary));
                } catch (IOException e) {				
                        e.printStackTrace();
                        
                }
        }else{
                try {
                        //load the jbin format
                        //loadedModel = (Node) BinaryImporter.getInstance().load(modelURL.openStream());
                        loadedModel =(Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
                } catch (IOException e) {
                        
                }
        }
        Quaternion temp = new Quaternion();
        temp.fromAngleAxis(FastMath.PI / 2, new Vector3f(-1, 0, 0));
        loadedModel.setLocalRotation(temp);
        loadedModel.setLocalTranslation(new Vector3f(0.0f,0.0f,0.0f));
        this.setRenderState(loadTexture());
        //loadedModel.setLocalScale(0.05f);
        
        //this.attachChild(loadedModel);
		AlphaState as = display.getRenderer().createAlphaState();
	    as.setBlendEnabled(false);
	    as.setTestEnabled(false);
	    this.setRenderState(as);
	    
	   
        this.attachChild(loadedModel);
        this.setLocalTranslation(new Vector3f(0.0f,0.0f,0.0f));
        
        
        /*i=null;
        try {
            i=(Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
        } catch (IOException e) {
        }*/
        /*((JointController) i.getController(0)).setSpeed(1.0f);
        ((JointController) i.getController(0)).setRepeatType(Controller.RT_CYCLE);
        i.setRenderQueueMode(Renderer.QUEUE_OPAQUE);*/
        //Node fire = new Effect_Fire(i);
        
        //Node fire = new Effect_Fire(i);
        
        //this.attachChild(i);
        //this.attachChild(fire);
        
        //this.attachChild(i);
        

	}
	
	public TextureState loadTexture(){
		URL texture_path = Mesh.class.getClassLoader().getResource(textureToLoad);
		
		//DisplaySystem display = DisplaySystem.getDisplaySystem();
        TextureState ts = display.getRenderer().createTextureState();
        ts.setTexture(TextureManager.loadTexture(texture_path ,Texture.MM_LINEAR,Texture.FM_LINEAR));
        ts.setEnabled(true);
		return ts;
	}
	
	public void setTexture(String textureToLoad){
		this.textureToLoad = textureToLoad;
		this.setRenderState(loadTexture());
	}
	
	public void onFire(boolean onfire){
		if(onfire == true){
			Node fire = new Effect_Fire(loadedModel);
	        this.attachChild(fire);
		}
	}
}
