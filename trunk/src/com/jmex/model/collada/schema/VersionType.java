/**
 * VersionType.java
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

public class VersionType extends SchemaString {
	public static final int E1_4_0 = 0; /* 1.4.0 */
	public static final int E1_4_1 = 1; /* 1.4.1 */

	public static String[] sEnumValues = {
		"1.4.0",
		"1.4.1",
	};

	public VersionType() {
		super();
	}

	public VersionType(String newValue) {
		super(newValue);
		validate();
	}

	public VersionType(SchemaString newValue) {
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
			throw new com.jmex.xml.xml.XmlException("Value of VersionType is invalid.");
	}
}
