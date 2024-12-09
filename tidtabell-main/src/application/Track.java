package application;

import java.util.ArrayList;

import javafx.scene.shape.Line;

public class Track {

	private double distance;	
	private ArrayList<Double> photoCells;
	
	public Track() {}

	public Track(double distance, ArrayList<Double> photoCells) {
		this.distance = distance;
		this.photoCells = photoCells;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public ArrayList<Double> getPhotoCells() {
		return photoCells;
	}

	public void setPhotoCells(ArrayList<Double> photoCells) {
		this.photoCells = photoCells;
	}
	
	public Line trackAsElement() {
	    return new Line(0, 0, distance, 0);
	}
}
