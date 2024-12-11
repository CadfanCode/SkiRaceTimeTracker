package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Serialize {

	private XMLEncoder xmlEncoder;
	private XMLDecoder xmlDecoder;
	private BufferedOutputStream outStream;
	private BufferedInputStream inputStream;
	private FileOutputStream fileOutStream;
	private FileInputStream fileInputStream; 
	private String fileName = "Skiers.xml";
	
	public Serialize() throws FileNotFoundException {
		this.fileOutStream = new FileOutputStream(fileName);
		this.fileInputStream = new FileInputStream(fileName);
		this.outStream = new BufferedOutputStream(fileOutStream); 
		this.inputStream = new BufferedInputStream(fileInputStream);
		this.xmlEncoder = new XMLEncoder(outStream);
		this.xmlDecoder = new XMLDecoder(inputStream);
	}	
	
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
	

	public void encoder (ArrayList<SerializableSkier> skiers) {
		for (SerializableSkier skier : skiers) {
			getXmlEncoder().writeObject(skier);			
		}
	}
	
	public ArrayList<SerializableSkier> decoder () {
		
		ArrayList<SerializableSkier> arraylist = new ArrayList<>();
		SerializableSkier skier = (SerializableSkier) getXmlDecoder().readObject();
		System.out.println(skier);

		return null;
	}
	
	
}