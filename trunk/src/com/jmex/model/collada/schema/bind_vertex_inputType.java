/**
 * bind_vertex_inputType.java
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

import com.jmex.xml.types.SchemaNCName;

public class bind_vertex_inputType extends com.jmex.xml.xml.Node {

	public bind_vertex_inputType(bind_vertex_inputType node) {
		super(node);
	}

	public bind_vertex_inputType(org.w3c.dom.Node node) {
		super(node);
	}

	public bind_vertex_inputType(org.w3c.dom.Document doc) {
		super(doc);
	}

	public bind_vertex_inputType(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	
	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "semantic" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "semantic", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "input_semantic" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "input_semantic", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "input_set" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "input_set", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "bind_vertex_input");
	}

	public static int getsemanticMinCount() {
		return 1;
	}

	public static int getsemanticMaxCount() {
		return 1;
	}

	public int getsemanticCount() {
		return getDomChildCount(Attribute, null, "semantic");
	}

	public boolean hassemantic() {
		return hasDomChild(Attribute, null, "semantic");
	}

	public SchemaNCName newsemantic() {
		return new SchemaNCName();
	}

	public SchemaNCName getsemanticAt(int index) throws Exception {
		return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "semantic", index)));
	}

	public org.w3c.dom.Node getStartingsemanticCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "semantic" );
	}

	public org.w3c.dom.Node getAdvancedsemanticCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "semantic", curNode );
	}

	public SchemaNCName getsemanticValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaNCName(getDomNodeValue(curNode));
	}

	public SchemaNCName getsemantic() throws Exception 
 {
		return getsemanticAt(0);
	}

	public void removesemanticAt(int index) {
		removeDomChildAt(Attribute, null, "semantic", index);
	}

	public void removesemantic() {
		removesemanticAt(0);
	}

	public org.w3c.dom.Node addsemantic(SchemaNCName value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "semantic", value.toString());
	}

	public org.w3c.dom.Node addsemantic(String value) throws Exception {
		return addsemantic(new SchemaNCName(value));
	}

	public void insertsemanticAt(SchemaNCName value, int index) {
		insertDomChildAt(Attribute, null, "semantic", index, value.toString());
	}

	public void insertsemanticAt(String value, int index) throws Exception {
		insertsemanticAt(new SchemaNCName(value), index);
	}

	public void replacesemanticAt(SchemaNCName value, int index) {
		replaceDomChildAt(Attribute, null, "semantic", index, value.toString());
	}

	public void replacesemanticAt(String value, int index) throws Exception {
		replacesemanticAt(new SchemaNCName(value), index);
	}

	public static int getinput_semanticMinCount() {
		return 1;
	}

	public static int getinput_semanticMaxCount() {
		return 1;
	}

	public int getinput_semanticCount() {
		return getDomChildCount(Attribute, null, "input_semantic");
	}

	public boolean hasinput_semantic() {
		return hasDomChild(Attribute, null, "input_semantic");
	}

	public SchemaNCName newinput_semantic() {
		return new SchemaNCName();
	}

	public SchemaNCName getinput_semanticAt(int index) throws Exception {
		return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "input_semantic", index)));
	}

	public org.w3c.dom.Node getStartinginput_semanticCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "input_semantic" );
	}

	public org.w3c.dom.Node getAdvancedinput_semanticCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "input_semantic", curNode );
	}

	public SchemaNCName getinput_semanticValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaNCName(getDomNodeValue(curNode));
	}

	public SchemaNCName getinput_semantic() throws Exception 
 {
		return getinput_semanticAt(0);
	}

	public void removeinput_semanticAt(int index) {
		removeDomChildAt(Attribute, null, "input_semantic", index);
	}

	public void removeinput_semantic() {
		removeinput_semanticAt(0);
	}

	public org.w3c.dom.Node addinput_semantic(SchemaNCName value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "input_semantic", value.toString());
	}

	public org.w3c.dom.Node addinput_semantic(String value) throws Exception {
		return addinput_semantic(new SchemaNCName(value));
	}

	public void insertinput_semanticAt(SchemaNCName value, int index) {
		insertDomChildAt(Attribute, null, "input_semantic", index, value.toString());
	}

	public void insertinput_semanticAt(String value, int index) throws Exception {
		insertinput_semanticAt(new SchemaNCName(value), index);
	}

	public void replaceinput_semanticAt(SchemaNCName value, int index) {
		replaceDomChildAt(Attribute, null, "input_semantic", index, value.toString());
	}

	public void replaceinput_semanticAt(String value, int index) throws Exception {
		replaceinput_semanticAt(new SchemaNCName(value), index);
	}

	public static int getinput_setMinCount() {
		return 0;
	}

	public static int getinput_setMaxCount() {
		return 1;
	}

	public int getinput_setCount() {
		return getDomChildCount(Attribute, null, "input_set");
	}

	public boolean hasinput_set() {
		return hasDomChild(Attribute, null, "input_set");
	}

	public uint newinput_set() {
		return new uint();
	}

	public uint getinput_setAt(int index) throws Exception {
		return new uint(getDomNodeValue(getDomChildAt(Attribute, null, "input_set", index)));
	}

	public org.w3c.dom.Node getStartinginput_setCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "input_set" );
	}

	public org.w3c.dom.Node getAdvancedinput_setCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "input_set", curNode );
	}

	public uint getinput_setValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new uint(getDomNodeValue(curNode));
	}

	public uint getinput_set() throws Exception 
 {
		return getinput_setAt(0);
	}

	public void removeinput_setAt(int index) {
		removeDomChildAt(Attribute, null, "input_set", index);
	}

	public void removeinput_set() {
		removeinput_setAt(0);
	}

	public org.w3c.dom.Node addinput_set(uint value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "input_set", value.toString());
	}

	public org.w3c.dom.Node addinput_set(String value) throws Exception {
		return addinput_set(new uint(value));
	}

	public void insertinput_setAt(uint value, int index) {
		insertDomChildAt(Attribute, null, "input_set", index, value.toString());
	}

	public void insertinput_setAt(String value, int index) throws Exception {
		insertinput_setAt(new uint(value), index);
	}

	public void replaceinput_setAt(uint value, int index) {
		replaceDomChildAt(Attribute, null, "input_set", index, value.toString());
	}

	public void replaceinput_setAt(String value, int index) throws Exception {
		replaceinput_setAt(new uint(value), index);
	}

}
