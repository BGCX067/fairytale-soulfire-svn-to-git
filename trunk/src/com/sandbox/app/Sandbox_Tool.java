package com.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.sandbox.app.GraphicWindowGL;
import com.sandbox.content.Area;
import com.sandbox.content.World;
import com.sandbox.gui.GUI_Button;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;


public class Sandbox_Tool extends JFrame implements ActionListener{
    
    public Sandbox_Tool(){
    	activearea = new Area("",0,false);
    	init();
    	setApp(this);
    }
    
    private void init(){
    	
    	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    	
    	this.setTitle("Sandbox");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setFont(new Font("Arial", 0, 12));
    	this.setSize(new Dimension(size));
    	this.setLocationRelativeTo(null);
    	this.setUndecorated(true);
        
        //createGUI();
    	addComponentsToPane(this.getContentPane());
    	createMenu();
        
        setVisible(true);
    }
    
    
    
/*******************************************GLOBAL COMMANDS******************************************/
    
    //Sandbox
    private static Sandbox_Tool app;
    
    
    //GraphicGL
    public boolean RIGHT_TO_LEFT = false;
    private String selectedTool = "Area Tool v0.2";
    private JPanel graphicGLInterfacePanel;
    
    
    public static Sandbox_Tool getApp(){
		return app;
	}
	
	public static void setApp(Sandbox_Tool new_app){
		app = new_app;
	}
    
    public void setSceneBorder(String tool){
    	if(tool.equals("WorldTool")){
    		selectedTool = "World Tool v0.1";
    	}
    	
    	if(tool.equals("AreaTool")){
    		selectedTool = "Area Tool v0.2";
    	}
    	
    	if(tool.equals("WeaponTool")){
    		selectedTool = "Weapon Tool v0.1";
    	}
    	
    	graphicGLInterfacePanel.setBorder(new TitledBorder(null, selectedTool,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
    }
   
    

	
	
	
	
	private World activeworld;
    private String worldname = "";
    
    public void setWorld(World world){
    	this.activeworld = world;

        worldname = " - " + activeworld.getName() + " - ";
        graphicGLInterfacePanel.setBorder(new TitledBorder(null, "Scene Editor" + worldname + areaname,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
    }
    
    public static Area activearea;
    private String areaname = "";
    
    public void createArea(String name, int size, boolean type){
    	activearea = new Area(name, size, type);
    	setActiveAreaName();
    }
    
    public void setActiveAreaName(){
    	
    	String type = "";
    	if(activearea.getType()==true){
    		type = "[AREA]";
    	}
    	else{
    		type = "[INTERIOR]";
    	}
    	
        areaname = " - " + activearea.getName()+ " " + type + " - ";
        graphicGLInterfacePanel.setBorder(new TitledBorder(null, "Area Tool" + worldname + areaname,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
    }
    
    
    
    

/**************************************************WINDOW GUI INTERFACE****************************************/
    
    public void createWindow(){
    	
    }
    
    private JCheckBoxMenuItem checkActionGridLines;
    
    public void createMenu(){
    	// Creates a menubar for a JFrame
        JMenuBar menuBar = new JMenuBar();
        // Add the menubar to the frame
        setJMenuBar(menuBar);
        
        // Define and add two drop down menu to the menubar
        JPopupMenu.
        setDefaultLightWeightPopupEnabled
        (false);
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");
        JMenu worldMenu = new JMenu("World");
        JMenu charMenu = new JMenu("Character");
        JMenu gameMenu = new JMenu("Gameplay");
        JMenu optionMenu = new JMenu("Options");
        JMenu toolMenu = new JMenu("Tools");
        JMenu helpMenu = new JMenu("Help");
        
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(worldMenu);
        menuBar.add(charMenu);
        menuBar.add(gameMenu);
        menuBar.add(optionMenu);
        menuBar.add(toolMenu);
        menuBar.add(helpMenu);
        
        
        // Create and add simple menu item to one of the drop down menu

        //FILE
        JMenuItem newAction = new JMenuItem("New WORLD");
        newAction.addActionListener(this);
        JMenuItem openAction = new JMenuItem("Open WORLD");
        openAction.addActionListener(this);
        JMenuItem saveAction = new JMenuItem("Save WORLD");
        saveAction.addActionListener(this);
        
        JMenuItem importAction = new JMenuItem("Import...");
        importAction.addActionListener(this);
        JMenuItem exportAction = new JMenuItem("Export...");
        exportAction.addActionListener(this);
        
        
        JMenuItem exitAction = new JMenuItem("Exit");
        exitAction.addActionListener(this);
        
        fileMenu.add(newAction);
        fileMenu.addSeparator();
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.addSeparator();
        fileMenu.add(importAction);
        fileMenu.add(exportAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        
        //VIEW
        
        checkActionGridLines = new JCheckBoxMenuItem("Grid Lines");
        checkActionGridLines.setSelected(true);
        checkActionGridLines.addActionListener(this);
        
        viewMenu.add(checkActionGridLines);
        
        //WORLD
        
        JMenuItem callWorldmap = new JMenuItem("Call WORLDMAP");
        callWorldmap.addActionListener(this);
        
        JMenuItem editWorld = new JMenuItem("Edit WORLD");
        editWorld.addActionListener(this);
        
        JMenuItem newArea = new JMenuItem("New AREA/INTERIOR");
        newArea.addActionListener(this);
        
        JMenuItem editArea = new JMenuItem("Edit AREA/INTERIOR");
        editArea.addActionListener(this);
        
        JMenuItem saveArea = new JMenuItem("Save AREA/INTERIOR");
        saveArea.addActionListener(this);
        
        JMenuItem loadArea = new JMenuItem("Load AREA/INTERIOR");
        loadArea.addActionListener(this);
        
        
        worldMenu.add(callWorldmap);
        worldMenu.add(editWorld);
        worldMenu.addSeparator();
        worldMenu.add(newArea);
        worldMenu.add(editArea);
        worldMenu.add(saveArea);
        worldMenu.add(loadArea);
        worldMenu.addSeparator();
        
        //TOOL
        
        JMenuItem callWorldCreator = new JMenuItem("WorldCreator");
        callWorldCreator.addActionListener(this);
        JMenuItem callAreaCreator = new JMenuItem("AreaCreator");
        callAreaCreator.addActionListener(this);
        JMenuItem callWeaponCreator = new JMenuItem("WeaponCreator");
        callWeaponCreator.addActionListener(this);
        
        toolMenu.add(callWorldCreator);
        toolMenu.add(callAreaCreator);
        toolMenu.add(callWeaponCreator);
        
        /*JMenuItem cutAction = new JMenuItem("Cut");
        JMenuItem copyAction = new JMenuItem("Copy");
        JMenuItem pasteAction = new JMenuItem("Paste");
        // Create and add CheckButton as a menu item to one of the drop down
        // menu
        
        // Create and add Radio Buttons as simple menu items to one of the drop
        // down menu
        JRadioButtonMenuItem radioAction1 = new JRadioButtonMenuItem(
                "Radio Button1");
        JRadioButtonMenuItem radioAction2 = new JRadioButtonMenuItem(
                "Radio Button2");
        // Create a ButtonGroup and add both radio Button to it. Only one radio
        // button in a ButtonGroup can be selected at a time.
        ButtonGroup bg = new ButtonGroup();
        bg.add(radioAction1);
        bg.add(radioAction2);
        
        
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.addSeparator();
        editMenu.add(radioAction1);
        editMenu.add(radioAction2);*/
        
        exitAction.addActionListener(this);
        
        /*newWorldAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	
            }
        });*/
    }
    
    
    
    
    
    
    public void addComponentsToPane(Container pane) {
        
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
        
        
        pane.add(createCommandBar(), BorderLayout.PAGE_START);
        
        
        
        
        
        //Make the center component big, since that's the
        //typical usage of BorderLayout.
        JButton button = new JButton("Button 2 (CENTER)");
        button.setPreferredSize(new Dimension(200, 100));
        //pane.add(button, BorderLayout.CENTER);
        
        
        graphicGLInterfacePanel = new JPanel();

        graphicGLInterfacePanel.setBorder(new TitledBorder(null, selectedTool,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        graphicGLInterfacePanel.setMinimumSize(new Dimension(100, 100));
        graphicGLInterfacePanel.setLayout(new BorderLayout());
        /***********************************************************/
        graphicGLInterfacePanel.add(getGlCanvas(), BorderLayout.CENTER);
        pane.add(graphicGLInterfacePanel, BorderLayout.CENTER);

        pane.add(createToolbar(), BorderLayout.LINE_START);
        
        button = new JButton("Button 5 (LINE_END)");
        pane.add(button, BorderLayout.PAGE_END);
        JProgressBar bar = new JProgressBar( 0, 100 );
        bar.setValue(36);
        bar.setStringPainted( true );
        pane.add(bar, BorderLayout.PAGE_END);
        
        button = new JButton("Long-Named Button 4 (Textures)");
        pane.add(createTextureBar(), BorderLayout.LINE_END);
        
    }
    
    public JPanel createCommandBar(){
    	JPanel commandtools = new JPanel();
        commandtools.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        
        
        GUI_Button button_create = new GUI_Button("com/sandbox/data/gui/icon_32_world.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        commandtools.add(button_create);
        button_create.setActionCommand("New WORLD");
        button_create.addActionListener(this);
        
        GUI_Button button_area = new GUI_Button("com/sandbox/data/gui/icon_32_worldarea.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        commandtools.add(button_area);
        button_area.setActionCommand("New AREA/INTERIOR");
        button_area.addActionListener(this);
        
        GUI_Button button_terrain = new GUI_Button("com/sandbox/data/gui/icon_32_terrain.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        commandtools.add(button_terrain);
        button_terrain.setActionCommand("Edit Terrain");
        button_terrain.addActionListener(this);
        
        GUI_Button button_toolbox = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        commandtools.add(button_toolbox);
        button_toolbox.setActionCommand("Toolbox");
        button_toolbox.addActionListener(this);
        
        GUI_Button button_light = new GUI_Button("com/sandbox/data/gui/icon_32_light.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        commandtools.add(button_light);
        button_light.setActionCommand("Ligth ON/OFF");
        button_light.addActionListener(this);
        
        
        GUI_Button button_raven = new GUI_Button("com/sandbox/data/gui/icon_48_raven.jpg",32,32);
        commandtools.add(button_raven);
        button_raven.setActionCommand("Start ProjectZero");
        button_raven.addActionListener(this);
        
        return commandtools;
    }
    
    public JPanel createToolbar(){
    	JPanel graphicInterfacePanel = new JPanel();
        
    	graphicInterfacePanel.setBorder(new TitledBorder(null, "Toolbox",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
    	graphicInterfacePanel.setPreferredSize(new Dimension(100,50));
        //graphicInterfacePanel.setLayout(new GridLayout(100,2));
    	
    	GUI_Button button_brush_terrain = new GUI_Button("com/sandbox/data/gui/icon_32_brush.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
    	button_brush_terrain.setActionCommand("Brush Terrain");
        graphicInterfacePanel.add(button_brush_terrain);
        button_brush_terrain.addActionListener(this);
        
        GUI_Button button_brush_alphatexture = new GUI_Button("com/sandbox/data/gui/icon_32_brush_alpha.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        button_brush_alphatexture.setActionCommand("Brush Alpha");
        graphicInterfacePanel.add(button_brush_alphatexture);
        button_brush_alphatexture.addActionListener(this);
        
        GUI_Button button_brush_texture = new GUI_Button("com/sandbox/data/gui/icon_32_brush_color.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        button_brush_texture.setActionCommand("Brush Texture");
        graphicInterfacePanel.add(button_brush_texture);
        button_brush_texture.addActionListener(this);
        
        GUI_Button button_space01 = new GUI_Button("com/sandbox/data/gui/icon_space.png", "com/sandbox/data/gui/icon_space.png",32,32);
        button_space01.setActionCommand("");
        graphicInterfacePanel.add(button_space01);
        button_space01.addActionListener(this);
        
        GUI_Button button_tab = new GUI_Button("com/sandbox/data/gui/icon_space.png", "com/sandbox/data/gui/icon_80x16_tab.jpg",80,8);
        button_tab.setActionCommand("");
        graphicInterfacePanel.add(button_tab);
        button_tab.addActionListener(this);
        
        GUI_Button button_brush_raise = new GUI_Button("com/sandbox/data/gui/icon_32_brush_raise.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        button_brush_raise.setActionCommand("Brush Raise");
        graphicInterfacePanel.add(button_brush_raise);
        button_brush_raise.addActionListener(this);
        
        GUI_Button button_brush_lower = new GUI_Button("com/sandbox/data/gui/icon_32_brush_lower.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        button_brush_lower.setActionCommand("Brush Lower");
        graphicInterfacePanel.add(button_brush_lower);
        button_brush_lower.addActionListener(this);
        
        GUI_Button button_brush_smooth = new GUI_Button("com/sandbox/data/gui/icon_32_brush_smooth.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        button_brush_smooth.setActionCommand("Brush Smooth");
        graphicInterfacePanel.add(button_brush_smooth);
        button_brush_smooth.addActionListener(this);
        
        GUI_Button button_brush_ripple = new GUI_Button("com/sandbox/data/gui/icon_32_brush_ripple.png", "com/sandbox/data/gui/icon_32.jpg",32,32);
        button_brush_ripple.setActionCommand("Brush Ripple");
        graphicInterfacePanel.add(button_brush_ripple);
        button_brush_ripple.addActionListener(this);
        
        
        
        
        return graphicInterfacePanel;
    }
    
    
    public JPanel createTextureBar(){
    	
    	
    	JPanel panel = new JPanel(); 
    	panel.setBorder(new TitledBorder(null, "Navigation:",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
    	panel.setPreferredSize(new Dimension(225, 300));
    	
    	
    	JPanel graphicInterfacePanel = new JPanel();
    	graphicInterfacePanel.setBorder(new TitledBorder(null, "Textures:",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
    	graphicInterfacePanel.setPreferredSize(new Dimension(50,1000));
        //graphicInterfacePanel.setLayout(new GridLayout(100,2));
    	
    	JScrollPane scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ); 
    	scrollpane.setPreferredSize(new Dimension(200,200));
    	scrollpane.setViewportView(graphicInterfacePanel);
    	panel.add(scrollpane);
    	
        
        GUI_Button button_brush_up = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",50,50);
        button_brush_up.setActionCommand("Brush Up");
        graphicInterfacePanel.add(button_brush_up);
        button_brush_up.addActionListener(this);
        button_brush_up.setEnabled(true);
        
        GUI_Button button_brush_down = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",50,50);
        button_brush_down.setActionCommand("Brush Down");
        graphicInterfacePanel.add(button_brush_down);
        button_brush_down.addActionListener(this);
        
        GUI_Button button_brush_smooth = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",50,50);
        button_brush_smooth.setActionCommand("Brush Smooth");
        graphicInterfacePanel.add(button_brush_smooth);
        button_brush_smooth.addActionListener(this);
        
        GUI_Button button_brush_ripple = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",50,50);
        button_brush_ripple.setActionCommand("Brush Ripple");
        graphicInterfacePanel.add(button_brush_ripple);
        button_brush_ripple.addActionListener(this);
        
        GUI_Button button_brush_texture = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",50,50);
        button_brush_texture.setActionCommand("Brush Texture");
        graphicInterfacePanel.add(button_brush_texture);
        button_brush_texture.addActionListener(this);
        
        GUI_Button button_brush_alphatexture = new GUI_Button("com/sandbox/data/gui/icon_32_toolbox.png", "com/sandbox/data/gui/icon_32.jpg",50,50);
        button_brush_alphatexture.setActionCommand("Brush Alphatexture");
        graphicInterfacePanel.add(button_brush_alphatexture);
        button_brush_alphatexture.addActionListener(this);
        
        
        
        return panel;
    }
    
    
    
/**************************************************Action Listener****************************************/    
    
    public void actionPerformed( ActionEvent event ) {
	    
    	if (event.getActionCommand().equals("Exit")) {
    		System.exit(0);
    	}
    	
    	
    	
    	if (event.getActionCommand().equals("New WORLD")) {
    		Window_Create_World window = new Window_Create_World();
    	}
    	
    	
    	if (event.getActionCommand().equals("New AREA/INTERIOR")) {
    		Window_Create_Area window = new Window_Create_Area();
    	}
    	
    	if (event.getActionCommand().equals("Load AREA/INTERIOR")) {
    		activearea.loadMap();
    	}
    	
    	
    	
    	if (event.getActionCommand().equals("Grid Lines")){
    		if (checkActionGridLines.isSelected()==true){
    			app.getGraphicWindowGL().enableGridNode(true);
    		}
    		if (checkActionGridLines.isSelected()==false){
    			app.getGraphicWindowGL().enableGridNode(false);
    		}
    	}
    	
    	if (event.getActionCommand() != null){
    		System.out.println("Pressed:" + event.getActionCommand());
    	}
	}    
    
    
    
    
/**************************************************GL INTERFACE****************************************/    
    /*protected Canvas getGlCanvas() {
        if (glCanvas == null) {

            // -------------GL STUFF------------------

            // make the canvas:
        	DisplaySystem display = DisplaySystem.getDisplaySystem(LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER);
        	display.registerCanvasConstructor("AWT", LWJGLAWTCanvasConstructor.class);
            glCanvas = (Canvas)display.createCanvas(width, height);

            // add a listener... if window is resized, we can do something about it.
            glCanvas.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent ce) {
                    impl.resizeCanvas(glCanvas.getSize().width, glCanvas
                            .getSize().height);
                }
            });
            glCanvas.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent arg0) {
                    ((AWTKeyInput) KeyInput.get()).setEnabled(true);
                    ((AWTMouseInput) MouseInput.get()).setEnabled(true);
                }

                public void focusLost(FocusEvent arg0) {
                    ((AWTKeyInput) KeyInput.get()).setEnabled(false);
                    ((AWTMouseInput) MouseInput.get()).setEnabled(false);
                }

            });

            // We are going to use jme's Input systems, so enable updating.
            ((JMECanvas) glCanvas).setUpdateInput(true);

            KeyInput.setProvider( InputSystem.INPUT_SYSTEM_AWT );
            ((AWTKeyInput) KeyInput.get()).setEnabled(false);
            KeyListener kl = (KeyListener) KeyInput.get();

            glCanvas.addKeyListener(kl);

            AWTMouseInput.setup( glCanvas, true );

            // Important!  Here is where we add the guts to the canvas:
            impl = new GraphicWindowGL(width, height);
            ((JMECanvas) glCanvas).setImplementor(impl);

            // -----------END OF GL STUFF-------------
        }
        return glCanvas;
    }*/
    
    private Canvas glCanvas;
    private int width = 640, height = 480;
    private GraphicWindowGL impl;
    private CameraHandler camhand;
    
    public GraphicWindowGL getGraphicWindowGL(){
    	return impl;
    }
    
    protected Canvas getGlCanvas() {
        
    	if (glCanvas == null) {

            // -------------GL STUFF------------------

            // make the canvas:
        	DisplaySystem display = DisplaySystem.getDisplaySystem(LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER);
        	display.registerCanvasConstructor("AWT", LWJGLAWTCanvasConstructor.class);
            glCanvas = (Canvas)display.createCanvas(width, height);
            glCanvas.setMinimumSize(new Dimension(100, 100));

            // add a listener... if window is resized, we can do something about
            // it.
            glCanvas.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent ce) {
                    doResize();
                }
            });

            camhand = new CameraHandler();

            glCanvas.addMouseWheelListener(camhand);
            glCanvas.addMouseListener(camhand);
            glCanvas.addMouseMotionListener(camhand);

            // Important! Here is where we add the guts to the canvas:
            impl = new GraphicWindowGL(width, height);

            ((JMECanvas) glCanvas).setImplementor(impl);

            // -----------END OF GL STUFF-------------

            Callable<Void> exe = new Callable<Void>() {
                public Void call() {
                    forceUpdateToSize();
                    ((JMECanvas) glCanvas).setTargetRate(60);
                    return null;
                }
            };
            GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).enqueue(exe);
        }
        return glCanvas;
    }
    
    
    public void forceUpdateToSize() {
        // force a resize to ensure proper canvas size.
        glCanvas.setSize(glCanvas.getWidth(), glCanvas.getHeight() + 1);
        glCanvas.setSize(glCanvas.getWidth(), glCanvas.getHeight() - 1);
    }
    
    protected void doResize() {
        if (impl != null) {
            impl.resizeCanvas(glCanvas.getWidth(), glCanvas.getHeight());
            if (impl.getCamera() != null) {
                Callable<Void> exe = new Callable<Void>() {
                    public Void call() {
                        impl.getCamera().setFrustumPerspective(
                                45.0f,
                                (float) glCanvas.getWidth()
                                        / (float) glCanvas.getHeight(), 1,
                                10000);
                        return null;
                    }
                };
                GameTaskQueueManager.getManager()
                        .getQueue(GameTaskQueue.RENDER).enqueue(exe);
            }
        }
    }
    
    
    
    
    /****************************************************************************************************/
    
    
    
    
    
    
    
    
    /************************************************CAMERA**********************************************/
    
    class CameraHandler extends MouseAdapter implements MouseMotionListener, MouseWheelListener {
        Point last = new Point(0, 0);
        Vector3f focus = new Vector3f();
        private Vector3f vector = new Vector3f();
        private Quaternion rot = new Quaternion();
        public Vector3f worldUpVector = Vector3f.UNIT_Y.clone();
        
        public CameraHandler(){
        }

        public void mouseDragged(final MouseEvent arg0) {
            Callable<Void> exe = new Callable<Void>() {
                public Void call() {
                    int difX = last.x - arg0.getX();
                    int difY = last.y - arg0.getY();
                    int mult = arg0.isShiftDown() ? 10 : 1;
                    last.x = arg0.getX();
                    last.y = arg0.getY();

                    int mods = arg0.getModifiers();
                    if ((mods & InputEvent.BUTTON1_MASK) != 0) {
                        rotateCamera(worldUpVector, difX * 0.0025f);
                        rotateCamera(impl.getRenderer().getCamera().getLeft(),
                                -difY * 0.0025f);
                    }
                    if ((mods & InputEvent.BUTTON2_MASK) != 0 && difY != 0) {
                        zoomCamera(difY * mult);
                    }
                    if ((mods & InputEvent.BUTTON3_MASK) != 0) {
                        panCamera(-difX, -difY);
                    }
                    return null;
                }
            };
            GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
                    .enqueue(exe);
        }

        public void mouseMoved(MouseEvent arg0) {
        }

        public void mousePressed(MouseEvent arg0) {
            last.x = arg0.getX();
            last.y = arg0.getY();
        }

        public void mouseWheelMoved(final MouseWheelEvent arg0) {
            Callable<Void> exe = new Callable<Void>() {
                public Void call() {
                    zoomCamera(arg0.getWheelRotation()
                            * (arg0.isShiftDown() ? -100 : -20));
                    return null;
                }
            };
            GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
                    .enqueue(exe);
        }

        public void recenterCamera() {
            Callable<Void> exe = new Callable<Void>() {
                public Void call() {
                    Camera cam = impl.getRenderer().getCamera();
                    Vector3f.ZERO.subtract(focus, vector);
                    cam.getLocation().addLocal(vector);
                    focus.addLocal(vector);
                    cam.lookAt(focus, worldUpVector );
                    cam.onFrameChange();
                    return null;
                }
            };
            GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
                    .enqueue(exe);
        }

        private void rotateCamera(Vector3f axis, float amount) {
            Camera cam = impl.getRenderer().getCamera();
            if (axis.equals(cam.getLeft())) {
                float elevation = -FastMath.asin(cam.getDirection().z);
                // keep the camera constrained to -89 -> 89 degrees elevation
                amount = Math.min(Math.max(elevation + amount,
                        -(FastMath.DEG_TO_RAD * 89)), (FastMath.DEG_TO_RAD * 89))
                        - elevation;
            }
            rot.fromAngleAxis(amount, axis);
            cam.getLocation().subtract(focus, vector);
            rot.mult(vector, vector);
            focus.add(vector, cam.getLocation());
            cam.lookAt(focus, worldUpVector );
        }

        private void panCamera(float left, float up) {
            Camera cam = impl.getRenderer().getCamera();
            cam.getLeft().mult(left, vector);
            vector.scaleAdd(up, cam.getUp(), vector);
            cam.getLocation().addLocal(vector);
            focus.addLocal(vector);
            cam.onFrameChange();
        }

        private void zoomCamera(float amount) {
            Camera cam = impl.getRenderer().getCamera();
            float dist = cam.getLocation().distance(focus);
            amount = dist - Math.max(0f, dist - amount);
            cam.getLocation().scaleAdd(amount, cam.getDirection(),
                    cam.getLocation());
            cam.onFrameChange();
        }
    }

}