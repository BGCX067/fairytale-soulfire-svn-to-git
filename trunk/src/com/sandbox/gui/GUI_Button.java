package com.sandbox.gui;

import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;

public class GUI_Button extends Component {
	
	private URL urlImage;
	private ImageIcon DEFAULT_BUTTON_ICON;
	private ImageIcon DEFAULT_BG_BUTTON_ICON;
	
	private Image image;
	private Image bg_image;
	boolean pressed = false;
	ActionListener actionListener;
	String actionCommand;
	
	private int sizex;
	private int sizey;
	
	public GUI_Button(String url){
		urlImage = GUI_Panel.class.getClassLoader().getResource(url);
		DEFAULT_BUTTON_ICON = new ImageIcon(urlImage);
		this.image = DEFAULT_BUTTON_ICON.getImage();
	    
		MediaTracker mt = new MediaTracker(this);
	    mt.addImage( image, 0 );
	    try {
	    	mt.waitForAll();
	    } catch (InterruptedException e) { /* error */ };
	    sizex = image.getWidth(null);
	    sizey = image.getWidth(null);
	    setSize( image.getWidth(null), image.getHeight(null) );
	    enableEvents( AWTEvent.MOUSE_EVENT_MASK );
	    
	    repaint();
	}
	
	public GUI_Button(String url, int x, int y){
		urlImage = GUI_Panel.class.getClassLoader().getResource(url);
		DEFAULT_BUTTON_ICON = new ImageIcon(urlImage);
		this.image = DEFAULT_BUTTON_ICON.getImage();
	    
		MediaTracker mt = new MediaTracker(this);
	    mt.addImage( image, 0 );
	    try {
	    	mt.waitForAll();
	    } catch (InterruptedException e) { /* error */ };
	    setSize( x, y);
	    sizex = x;
	    sizey = y;
	    enableEvents( AWTEvent.MOUSE_EVENT_MASK );
	    
	    repaint();
	}
	
	public GUI_Button(String url, String url_bg, int x, int y){
		urlImage = GUI_Panel.class.getClassLoader().getResource(url);
		DEFAULT_BUTTON_ICON = new ImageIcon(urlImage);
		this.image = DEFAULT_BUTTON_ICON.getImage();
		
		urlImage = GUI_Panel.class.getClassLoader().getResource(url_bg);
		DEFAULT_BG_BUTTON_ICON = new ImageIcon(urlImage);
		this.bg_image = DEFAULT_BG_BUTTON_ICON.getImage();
	    
		MediaTracker mt = new MediaTracker(this);
	    mt.addImage( bg_image, 0 );
	    mt.addImage( image, 1 );
	    try {
	    	mt.waitForAll();
	    } catch (InterruptedException e) { /* error */ };
	    setSize( x, y);
	    sizex = x;
	    sizey = y;
	    enableEvents( AWTEvent.MOUSE_EVENT_MASK );
	    
	    repaint();
	}
	
	public void paint( Graphics g ) {
		//int width = getSize().width, height = getSize().height;
	    //int offset = pressed ? 0 : 2;
		g.drawImage(bg_image, 0, 0, sizex, sizey, this );
		g.drawImage(image, 0, 0, sizex, sizey, this );
	}
	
	public Dimension getPreferredSize() {
		return getSize();
	}

	public void processEvent( AWTEvent e ) {
		if ( e.getID() == MouseEvent.MOUSE_PRESSED ) {
			pressed = true;
			repaint();
	    } else
	    if ( e.getID() == MouseEvent.MOUSE_RELEASED ) {
	    	pressed = false;
	      	repaint();
	      	fireEvent();
	    }
	    super.processEvent(e);
	}

	public void setActionCommand( String actionCommand ) {
		this.actionCommand = actionCommand;
	}
	public void addActionListener(ActionListener l) {
		actionListener = AWTEventMulticaster.add(actionListener, l);
	}
	public void removeActionListener(ActionListener l) {
		actionListener = AWTEventMulticaster.remove(actionListener, l);
	}
	private void fireEvent() {
		if (actionListener != null) {
			ActionEvent event = new ActionEvent( this,
			ActionEvent.ACTION_PERFORMED, actionCommand );
			actionListener.actionPerformed( event );
	    }
	}
}
