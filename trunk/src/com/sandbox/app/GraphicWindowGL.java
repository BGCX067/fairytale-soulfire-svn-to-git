package com.sandbox.app;


import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.util.stat.StatCollector;
import com.jmex.terrain.TerrainPage;

public class GraphicWindowGL extends SimpleCanvasImpl {
	
    public GraphicWindowGL(int width, int height) {
        super(width, height);
    }

    public void simpleSetup() {
    	
    	setupIni();
        setupCam();
    	setupInput();
    	setupEnvironment();
    }
    
    
    private DisplaySystem display;
    private Camera cam;
    
    public void setupIni(){
    	display = DisplaySystem.getDisplaySystem();
    	cam = Sandbox_Tool.getApp().getGraphicWindowGL().getRenderer().getCamera();
    }
    
    public TerrainPage page;
    
    public void setupCam(){
    	cam.setLocation(new Vector3f(0.0f,200.0f,400.0f));
    }
    
    public void simpleRender(){
    	
    }
    
    public void simpleUpdate() {
    	if(Sandbox_Tool.getApp().activearea.updateArea()){
    		rootNode.detachChildNamed("Area");
        	rootNode.attachChild(Sandbox_Tool.getApp().activearea.getArea());
        	Sandbox_Tool.getApp().activearea.isUpdated();
    	}
    	
    	
    }
    
    private void setupEnvironment(){
    	rootNode.attachChild(Sandbox_Tool.getApp().activearea.getArea());
    }
    
    private void setupInput() {
    	rootNode.attachChild(getGridNode());
    }
   
/****************************************GRID LAYOUT********************************************************/    
    
    private static int GRID_LINES = 51;
    private static float GRID_SPACING = 10f;
    
    public void enableGridNode(boolean activ){
    	if(activ == false){
    		rootNode.detachChildNamed("GridLines");
    	}
    	
    	if(activ == true){
    		rootNode.detachChildNamed("GridLines");
    		rootNode.attachChild(getGridNode());
    	}
    }
    
   
    
    private Node getGridNode(){
    	Node gridNode = createGridNode();
    	return gridNode;
    }
    
    private Node getGridNodeStandard(){
    	GRID_LINES = 51;
    	GRID_SPACING = 10f;
    	
    	Node gridNode = createGridNode();
    	return gridNode;
    }
    
    private Node getGridNode(int grid_lines, float grid_spacing){
    	GRID_LINES = grid_lines;
    	GRID_SPACING = grid_spacing;
    	
    	Node gridNode = createGridNode();
    	return gridNode;
    }
    
    private Node createGridNode(){
    	Node gridNode = new Node("GridLines");
    	Geometry grid = createGrid();
        gridNode.attachChild(grid);
    	
    	return gridNode;
    }
    
    private Geometry createGrid() {
        Vector3f[] vertices = new Vector3f[GRID_LINES * 2 * 2];
        float edge = GRID_LINES / 2 * GRID_SPACING;
        for (int ii = 0, idx = 0; ii < GRID_LINES; ii++) {
            float coord = (ii - GRID_LINES / 2) * GRID_SPACING;
            vertices[idx++] = new Vector3f(-edge, 0f, coord);
            vertices[idx++] = new Vector3f(+edge, 0f, coord);
            vertices[idx++] = new Vector3f(coord, 0f, -edge);
            vertices[idx++] = new Vector3f(coord, 0f, +edge);
        }
        Geometry grid = new com.jme.scene.Line("grid", vertices, null,
                null, null) {
            private static final long serialVersionUID = 1L;
            @Override
            public void draw(Renderer r) {
                StatCollector.pause();
                super.draw(r);
                StatCollector.resume();
            }
        };
        grid.getDefaultColor().set(ColorRGBA.darkGray.clone());
        return grid;
    }
}