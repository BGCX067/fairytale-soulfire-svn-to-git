package com.sandbox.gui;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GUI_Panel extends JPanel {
	
	private URL urlImage;
	private ImageIcon DEFAULT_BACKGROUND_ICON;
	
	public GUI_Panel(String url){
		urlImage = GUI_Panel.class.getClassLoader().getResource(url);
		DEFAULT_BACKGROUND_ICON = new ImageIcon(urlImage);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		Composite oldComp = g2d.getComposite();

		//Composite alphaComp = AlphaComposite.getInstance(
				//AlphaComposite.SRC_OVER, 0.3f);

		//g2d.setComposite(alphaComp);
		
		g2d.drawImage(DEFAULT_BACKGROUND_ICON.getImage(), 0, 0, this.getWidth(), this.getHeight(),
				this);

		g2d.setComposite(oldComp);
	}
}
