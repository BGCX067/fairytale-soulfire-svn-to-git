package game.bin.loader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

public class FileUtil{
	DataOutputStream dos;
	String input;
	String clean_input;
	
	public FileUtil(String fileName){
		input = readFromFile(fileName);
		clean_input = cleanInput();
		//System.out.println(clean_input);
	}
	
	/*
	 * Utility method to write a given text to a file
	 */
	public boolean writeToFile(String fileName, String dataLine, boolean isAppendMode, boolean isNewLine) {
		if (isNewLine) {
			dataLine = "\n" + dataLine;
		}

		try {
			File outFile = new File(fileName);
			if (isAppendMode) {
				dos = new DataOutputStream(new FileOutputStream(fileName, true));
			} else {
				dos = new DataOutputStream(new FileOutputStream(outFile));
			}
			dos.writeBytes(dataLine);
			dos.close();
		} catch (FileNotFoundException ex) {
			return (false);
		} catch (IOException ex) {
			return (false);
		}
		return (true);
	}

	/*
	 * Reads data from a given file
	 */
	public String readFromFile(String fileName) {
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
	
	public String cleanInput(){
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
	
	public String searchFromFile(String search){	

		int size = (clean_input.indexOf(search+"=") + search.length()+1);
		String value1 = clean_input.substring( size, clean_input.length());

		String value2 = clean_input.substring((clean_input.indexOf(search+"=") + search.length()+1), (size + value1.indexOf("\n")));
		return value2; 
	}
	
	public int[] convertArrayString(String convert) {
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
	
	public int[] copy(int[] array){
		
		for(int i = 0; i < array.length; i++){
			System.out.println(array[i] + " " + array.length);
		}
		
		return null;
	}
	
	public boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	
	public boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return file.delete();
  	}
	
	/*
	 * Reads data from a given file into a Vector
	 */

	public Vector fileToVector(String fileName) {
		Vector v = new Vector();
		String inputLine;
		try {
			File inFile = new File(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));

			while ((inputLine = br.readLine()) != null) {
				v.addElement(inputLine.trim());
			}
			br.close();
		} // Try
		catch (FileNotFoundException ex) {
			//
		} catch (IOException ex) {
			//
		}
		return (v);
	}

	/*
	 * Writes data from an input vector to a given file
	 */

	public void vectorToFile(Vector v, String fileName) {
		for (int i = 0; i < v.size(); i++) {
			writeToFile(fileName, (String) v.elementAt(i), true, true);
		}
	}

	/*
	 * Copies unique rows from a source file to a destination file
	 */

	public void copyUniqueElements(String sourceFile, String resultFile) {
		Vector v = fileToVector(sourceFile);
		v = MiscUtil.removeDuplicates(v);
		vectorToFile(v, resultFile);
	}
	
} // end FileUtil

class MiscUtil {

	public static boolean hasDuplicates(Vector v) {
		int i = 0;
		int j = 0;
		boolean duplicates = false;

		for (i = 0; i < v.size() - 1; i++) {
			for (j = (i + 1); j < v.size(); j++) {
				if (v.elementAt(i).toString().equalsIgnoreCase(
						v.elementAt(j).toString())) {
					duplicates = true;
				}
			}
		}
		return duplicates;
	}

	public static Vector removeDuplicates(Vector s) {
		int i = 0;
		int j = 0;
		boolean duplicates = false;

		Vector v = new Vector();

		for (i = 0; i < s.size(); i++) {
			duplicates = false;
			for (j = (i + 1); j < s.size(); j++) {
				if (s.elementAt(i).toString().equalsIgnoreCase(
						s.elementAt(j).toString())) {
					duplicates = true;
				}
			}
			if (duplicates == false) {
				v.addElement(s.elementAt(i).toString().trim());
			}
		}
		return v;
	}

	public static Vector removeDuplicateDomains(Vector s) {
		int i = 0;
		int j = 0;
		boolean duplicates = false;
		String str1 = "";
		String str2 = "";

		Vector v = new Vector();

		for (i = 0; i < s.size(); i++) {
			duplicates = false;
			for (j = (i + 1); j < s.size(); j++) {
				str1 = "";
				str2 = "";
				str1 = s.elementAt(i).toString().trim();
				str2 = s.elementAt(j).toString().trim();
				if (str1.indexOf('@') > -1) {
					str1 = str1.substring(str1.indexOf('@'));
				}
				if (str2.indexOf('@') > -1) {
					str2 = str2.substring(str2.indexOf('@'));
				}

				if (str1.equalsIgnoreCase(str2)) {
					duplicates = true;
				}
			}
			if (duplicates == false) {
				v.addElement(s.elementAt(i).toString().trim());
			}
		}
		return v;
	}

	public static boolean areVectorsEqual(Vector a, Vector b) {
		if (a.size() != b.size()) {
			return false;
		}

		int i = 0;
		int vectorSize = a.size();
		boolean identical = true;
	
	    for (i = 0; i < vectorSize; i++) {
	    	if (!(a.elementAt(i).toString().equalsIgnoreCase(b.elementAt(i).toString()))) {
	    		identical = false;
	    	}
	    }
	    return identical;
	}

	public static Vector removeDuplicates(Vector a, Vector b) {
		
		int i = 0;
		int j = 0;
		boolean present = true;
		Vector v = new Vector();
		
		for (i = 0; i < a.size(); i++) {
			present = false;
			for (j = 0; j < b.size(); j++) {
				if (a.elementAt(i).toString().equalsIgnoreCase(
						b.elementAt(j).toString())) {
					present = true;
				}
			}
			if (!(present)) {
				v.addElement(a.elementAt(i));
			}
		}
		return v;
	}
}
