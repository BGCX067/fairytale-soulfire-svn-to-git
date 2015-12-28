package com.sandbox.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileClass {
	
	File data;
	String input;
	
	public FileClass(String fileName){
		 data = new File(fileName);
		 read();
		 clean();
		 
	}
	
	
	public void read() {
		String output = "";
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(data)));
			
			//DataLine = br.readLine();
			
			String strline;
			while ((strline = br.readLine()) != null)   {
				output = output + strline + "\n";
			}
			
			br.close();
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}
		
		input = output;
	}
	
	public void clean(){
		String value = "";
		boolean valid = true;
		boolean name = false;
		
		for(int i = 0; i < input.length(); i++){
			
			
			if(input.charAt(i) == '"' && name == false){
				name = true;
			} else {
			if(input.charAt(i) == '"' && name == true){
				name = false;
			}}
			
			if(input.charAt(i) != ' ' && input.charAt(i) != '\t' || name == true){
				
				if(input.charAt(i) == '['){
					valid = false;
				}
				if(valid == true && input.charAt(i) != '"'){
					if(i != 0){
						if(input.charAt(i-1) == '\n' && input.charAt(i) == '\n'){
							
						} else {
						if(input.charAt(i-1) == ',' && input.charAt(i) == '\n'){
							
						}else {
							value = value + input.charAt(i);
						}}
					} 
					else {
						value = value + input.charAt(i);
					}
				}
				if(input.charAt(i) == ']'){
					valid = true;
				}
			}
		}
		
		if(value.charAt(0) == '\n'){
			value = value.substring(1, value.length());
		}
		
		input = value; 
	}
	
	public String search(String search){	

		int size = (input.indexOf(search+"=") + search.length()+1);
		String value1 = input.substring( size, input.length());

		String value2 = input.substring((input.indexOf(search+"=") + search.length()+1), (size + value1.indexOf("\n")));
		return value2; 
	}
}
