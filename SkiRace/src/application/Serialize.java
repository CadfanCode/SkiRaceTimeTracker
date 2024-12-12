package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Serialize implements Serializable {

	private static final long serialVersionUID = 1L;
	private XMLEncoder xmlEncoder;
	private XMLDecoder xmlDecoder;
	private String fileName = "Skiers.xml";
	
	public Serialize() {}	
	
	public Serialize(XMLEncoder xmlEncoder, XMLDecoder xmlDecoder) {
		super();
		this.xmlEncoder = xmlEncoder;
		this.xmlDecoder = xmlDecoder;
	}

	public XMLEncoder getXmlEncoder() {
		return xmlEncoder;
	}

	public void setXmlEncoder(XMLEncoder xmlEncoder) {
		this.xmlEncoder = xmlEncoder;
	}

	public XMLDecoder getXmlDecoder() {
		return xmlDecoder;
	}

	public void setXmlDecoder(XMLDecoder xmlDecoder) {
		this.xmlDecoder = xmlDecoder;
	}
	

	public void encodeObject(ArrayList<SerializableSkier> skiers) throws FileNotFoundException {
		System.out.println("Encoding started.");
	    try (FileOutputStream fos = new FileOutputStream(fileName);
	         BufferedOutputStream bos = new BufferedOutputStream(fos);
	         XMLEncoder encoder = new XMLEncoder(bos)) {
	    	System.out.println("Writing file to: " + new java.io.File(fileName).getAbsolutePath()); // Log the absolute path
	    	for (SerializableSkier skier : skiers) {
	    		encoder.writeObject(skier);	    		
	    	}
	    	System.out.println("Encoding ended successfully.");
	    	
	    } catch (Exception e) {
	        System.err.println("Error during encoding: " + e.getMessage());
	    }
	}
	
	public ArrayList<SerializableSkier> decodeObject(int size) {
		System.out.println("Decoder started.");
		ArrayList<SerializableSkier> skiers = new ArrayList<>();
		
	    try (FileInputStream fis = new FileInputStream(fileName);
	         BufferedInputStream bis = new BufferedInputStream(fis);
	         XMLDecoder decoder = new XMLDecoder(bis)) {
	    	System.out.println("Retrieving file from: " + new java.io.File(fileName).getAbsolutePath()); // Log the absolute path
	    	for (int i = 0; i < size; i++) {
	    		skiers.add((SerializableSkier) decoder.readObject());	    		
	    	}
	    	System.out.println("Decoder ended.");
	    } catch (Exception e) {
	        System.err.println("Error during decoding: " + e.getMessage());
	    }
	    
	    return skiers;
	}	
	
}