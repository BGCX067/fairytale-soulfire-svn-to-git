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

public class Window_Create_Area extends JFrame implements ActionListener {
	
	private final String[] TYPE = {
			"Area", "Interior"
		};
		
	private final String[] SIZE = {
		"16x16", "32x32", "64x64", "128x128"};
		
	private Sandbox_Tool app;
		
	public Window_Create_Area(){
			
		this.setTitle("Create Area/Interior");
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
	private JComboBox combo,combotype;
	
	public void createGUI(){
			
		Container cp = getContentPane();
		JPanel panel = new JPanel();
			
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Area/Interior:",
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
		
		
		JLabel typelabel = new JLabel("Type: ");
		typelabel.setLocation(25,80);
		typelabel.setSize(100,25);
		panel.add(typelabel);
		combotype = new JComboBox(TYPE);
		combotype.setLocation(25,110);
		combotype.setSize(100,25);
		panel.add(combotype);
			
		
		JLabel sizelabel = new JLabel("Size: ");
		sizelabel.setLocation(25,140);
		sizelabel.setSize(100,25);
		panel.add(sizelabel);
		combo = new JComboBox(SIZE);
		combo.setLocation(25,170);
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
			boolean type;
			
			if(combotype.getSelectedIndex()==0){
				type = true;
			}
			else{
				type = false;
			}
			
			if(combo.getSelectedIndex()==0){
				size = 16;
			}
			if(combo.getSelectedIndex()==1){
				size = 32;
			}
			if(combo.getSelectedIndex()==2){
				size = 64;
			}
			if(combo.getSelectedIndex()==3){
				size = 128;
			}
			
			if(textname.getText().equals("")){
				name = "NO AREA NAME";
			}
			else{
				name = textname.getText();
			}
			app.createArea(name, size, type);
			this.dispose();
		}
		if (e.getActionCommand().equals("CANCEL")) {
			this.dispose();
		}
	}

}
