package com.sandbox.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileEditor {

	
	/*Writer output = null;
    String text = "Rajesh Kumar";
    File file = new File("write.txt");
    output = new BufferedWriter(new FileWriter(file));
    output.write(text);
    output.close();
    System.out.println("Your file has been written"); */
	
	public static String readFromFile(String fileName) {
		String dataline = "";
		
		try {
			File inFile = new File(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			
			//DataLine = br.readLine();
			
			String strline;
			while ((strline = br.readLine()) != null)   {
				dataline = dataline + strline + "\n";
			}
			
			br.close();
		} catch (FileNotFoundException ex) {
			return (null);
		} catch (IOException ex) {
			return (null);
		}
		
		return (dataline);
	}
	
	public static String cleanInput(String input){
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
		
		return value; 
	}
	
	public String searchFromFile(String search, String input){	

		int size = (input.indexOf( search + "=") + search.length()+1);
		String value1 = input.substring( size, input.length());

		String value2 = input.substring((input.indexOf(search + "=") + search.length()+1), (size + value1.indexOf("\n")));
		return value2; 
	}
	
	public int[] convertArrayStringToInt(String convert) {
		//ArrayList<Integer> elements = new ArrayList();
		ArrayList<Integer> elements = new ArrayList<Integer>();
		String number = "";
		
		for(int i = 0; i < convert.length(); i++){
			if(convert.charAt(i) == ','){
				elements.add(Integer.parseInt(number));
				number = "";
			} else {
				number = number + convert.charAt(i); 
			}
		}
		elements.add(Integer.parseInt(number));
		
		int[] iArray = new int[elements.size()];
		for (int i=0; i < elements.size();i++ ) {
            Integer in = (Integer) elements.get(i);
            iArray[i] = in.intValue();
        }
		
		return iArray;
	}
	
	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return file.delete();
  	}
}
