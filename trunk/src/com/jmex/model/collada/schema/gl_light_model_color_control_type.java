/**
 * gl_light_model_color_control_type.java
 *
 * This file was generated by XMLSpy 2007sp2 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSpy Documentation for further details.
 * http://www.altova.com/xmlspy
 */


package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaString;

public class gl_light_model_color_control_type extends SchemaString {
	public static final int ESINGLE_COLOR = 0; /* SINGLE_COLOR */
	public static final int ESEPARATE_SPECULAR_COLOR = 1; /* SEPARATE_SPECULAR_COLOR */

	public static String[] sEnumValues = {
		"SINGLE_COLOR",
		"SEPARATE_SPECULAR_COLOR",
	};

	public gl_light_model_color_control_type() {
		super();
	}

	public gl_light_model_color_control_type(String newValue) {
		super(newValue);
		validate();
	}

	public gl_light_model_color_control_type(SchemaString newValue) {
		super(newValue);
		validate();
	}

	public static int getEnumerationCount() {
		return sEnumValues.length;
	}

	public static String getEnumerationValue(int index) {
		return sEnumValues[index];
	}

	public static boolean isValidEnumerationValue(String val) {
		for (int i = 0; i < sEnumValues.length; i++) {
			if (val.equals(sEnumValues[i]))
				return true;
		}
		return false;
	}

	public void validate() {

		if (!isValidEnumerationValue(toString()))
			throw new com.jmex.xml.xml.XmlException("Value of gl_light_model_color_control_type is invalid.");
	}
}
