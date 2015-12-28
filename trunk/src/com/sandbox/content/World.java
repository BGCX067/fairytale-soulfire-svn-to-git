package com.sandbox.content;

public class World {

	private String name;
	private int size;
	
	
	public World(String name, int size){
		this.name = name;
		this.size = size;
	}
	
	public String getName(){
		return name;
	}
	
	public int getSize(){
		return size;
	}
}
