package game.bin.gamesys;

import com.jme.scene.state.GLSLShaderObjectsState;
import com.jmex.physics.PhysicsSpace;

public class RuntimeCash {
	
	//Graphic
	private static boolean NORMAL_MAPPING = true;
	private static boolean VSYNCRO = false;
	
	private static int ANTIALIASING = 4;
	
	private static PhysicsSpace PHYSICS_SPACE;
	private static GLSLShaderObjectsState GLSL_NORMALMAP;
	
	
	public RuntimeCash(){

	}
	
	public static void setPhysicsSpace(PhysicsSpace physicsspace){
		PHYSICS_SPACE = physicsspace;
	}
	
	public static PhysicsSpace getPhysicsSpace(){
		return PHYSICS_SPACE;
	}
	
	public static boolean getNormalMapping(){
		return NORMAL_MAPPING;
	}
	
	public static void setNormalMapping(boolean NORMAL_MAPPING){
		RuntimeCash.NORMAL_MAPPING = NORMAL_MAPPING;
	}
	
	public static GLSLShaderObjectsState getGLSLShaderObjectNormalMap(){
		return GLSL_NORMALMAP;
	}
	
	public static void setGLSLShaderObjectNormalMap(GLSLShaderObjectsState GLSL_NORMALMAP){
		RuntimeCash.GLSL_NORMALMAP = GLSL_NORMALMAP;
	}
	
	public static boolean getVSyncro(){
		return VSYNCRO;
	}
	
	public static int getAntiAliasing(){
		return ANTIALIASING;
	}
}
