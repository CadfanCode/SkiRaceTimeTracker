package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Serialize {

	private XMLEncoder xmlEncoder;
	private XMLDecoder xmlDecoder;
	private BufferedOutputStream outStream;
	private BufferedInputStream inputStream;
	private FileOutputStream fileOutStream;
	private FileInputStream fileInputStream; 
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
	    try (FileOutputStream fos = new FileOutputStream(fileName);
	         BufferedOutputStream bos = new BufferedOutputStream(fos);
	         XMLEncoder encoder = new XMLEncoder(bos)) {
	        
	    	for (SerializableSkier skier : skiers) {
	    		encoder.writeObject(skier);	    		
	    	}
	    	
	    } catch (Exception e) {
	        System.err.println("Error during encoding: " + e.getMessage());
	    }
	}
	
	public ArrayList<SerializableSkier> decodeObject(int size) {
		ArrayList<SerializableSkier> skiers = new ArrayList<>();
		
	    try (FileInputStream fis = new FileInputStream(fileName);
	         BufferedInputStream bis = new BufferedInputStream(fis);
	         XMLDecoder decoder = new XMLDecoder(bis)) {
	    	
	    	for (int i = 0; i < size; i++) {
	    		skiers.add((SerializableSkier) decoder.readObject());	    		
	    	}
	    		        
	    } catch (Exception e) {
	        System.err.println("Error during decoding: " + e.getMessage());
	    }
	    
	    return skiers;
	}	
	
}