package com.sandbox.util;

import java.util.ArrayList;

public class Converter {
	
	public static int[] Array_StringToInt(String input) {
		//ArrayList<Integer> elements = new ArrayList();
		ArrayList<Integer> elements = new ArrayList<Integer>();
		String number = "";
		
		for(int i = 0; i < input.length(); i++){
			if(input.charAt(i) == ','){
				elements.add(Integer.parseInt(number));
				number = "";
			} else {
				number = number + input.charAt(i); 
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
	
	public static float[] Array_StringToFloat(String input) {
		ArrayList<Float> elements = new ArrayList<Float>();
		String number = "";
		
		for(int i = 0; i < input.length(); i++){
			if(input.charAt(i) == ','){
				elements.add(Float.parseFloat(number));
				number = "";
			} else {
				number = number + input.charAt(i); 
			}
		}
		elements.add(Float.parseFloat(number));
		
		float[] fArray = new float[elements.size()];
		for (int i=0; i < elements.size();i++ ) {
            float in = (float) elements.get(i);
            fArray[i] = in;
        }
		
		return fArray;
	}
}
