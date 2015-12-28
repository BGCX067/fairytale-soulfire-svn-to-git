package com.sandbox.app;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.sandbox.content.World;

public class Window_Create_World extends JFrame implements ActionListener{
	
	private final String[] TYPE = {
			"World", "Area", "Interior"
		};
		
	private final String[] SIZE = {
		"128x128", "256x256", "512x512", "1024x1024"};
		
	private Sandbox_Tool app;
		
	public Window_Create_World(){
			
		this.setTitle("Create WORLD");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setFont(new Font("Arial", 0, 12));
	    this.setSize(new Dimension(400,300));
	    this.setLocationRelativeTo(null);
	    this.setUndecorated(true);
	    this.setBackground(Color.lightGray);
	    	
	    this.app = Sandbox_Tool.getApp();
	    
	     
	    createGUI();    	
	    setVisible(true);
	}
		
	public void init(){
	}
	
	private JTextField textname;
	private JComboBox combo;
	
	public void createGUI(){
			
		Container cp = getContentPane();
		JPanel panel = new JPanel();
			
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "WORLD:",
	            TitledBorder.DEFAULT_JUSTIFICATION,
	            TitledBorder.DEFAULT_POSITION, null, null));
			
		JLabel namelabel = new JLabel("Name: ");
		namelabel.setLocation(25,20);
		namelabel.setSize(100,25);
		panel.add(namelabel);
		textname = new JTextField();
		textname.setLocation(25,50);
		textname.setSize(100,25);
		panel.add(textname);
			
			
		JLabel sizexlabel = new JLabel("Size: ");
		sizexlabel.setLocation(25,80);
		sizexlabel.setSize(100,25);
		panel.add(sizexlabel);
		combo = new JComboBox(SIZE);
		combo.setLocation(25,110);
		combo.setSize(100,25);
		panel.add(combo);
			
			
		JButton create = new JButton("CREATE");
		create.setLocation(25,235);
		create.setSize(150,30);
		panel.add(create);
		create.addActionListener(this);
			
		JButton cancel = new JButton("CANCEL");
		cancel.setLocation(225,235);
		cancel.setSize(150,30);
		panel.add(cancel);
		cancel.addActionListener(this);
			
		cp.add(panel);
			
	}
		
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("CREATE")) {
			int size = 0;
			String name;
			
			if(combo.getSelectedIndex()==0){
				size = 128;
			}
			if(combo.getSelectedIndex()==1){
				size = 256;
			}
			if(combo.getSelectedIndex()==2){
				size = 512;
			}
			if(combo.getSelectedIndex()==3){
				size = 1024;
			}
			if(textname.getText().equals("")){
				name = "NO WORLD NAME";
			}
			else{
				name = textname.getText();
			}
			app.setWorld(new World(name, size));
			this.dispose();
		}
		if (e.getActionCommand().equals("CANCEL")) {
			this.dispose();
		}
	}

}

