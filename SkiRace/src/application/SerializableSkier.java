package application;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

public class SerializableSkier implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int startNumber;
    private double speed = 0.0;
    private boolean finished = false;
    private String startType;
    private int raceDistance;
    private String lastCheckPointTime;
    private String timeFromLeader;
    private String finishTime;
    private int distance;
    
    public SerializableSkier() {}

	public SerializableSkier(String name, int startNumber, double speed, boolean finished, String startType,
		int raceDistance, String lastCheckPointTime, String timeFromLeader, String finishTime, int distance) {
		this.name = name;
		this.startNumber = startNumber;
		this.speed = speed;
		this.finished = finished;
		this.startType = startType;
		this.raceDistance = raceDistance;
		this.lastCheckPointTime = lastCheckPointTime;
		this.timeFromLeader = timeFromLeader;
		this.finishTime = finishTime;
		this.distance = distance;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStartNumber() {
		return startNumber;
	}
	public void setStartNumber(int startNumber) {
		this.startNumber = startNumber;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public String getStartType() {
		return startType;
	}
	public void setStartType(String startType) {
		this.startType = startType;
	}
	public int getRaceDistance() {
		return raceDistance;
	}
	public void setRaceDistance(int raceDistance) {
		this.raceDistance = raceDistance;
	}
	public String getLastCheckPointTime() {
		return lastCheckPointTime;
	}
	public void setLastCheckPointTime(String lastCheckPointTime) {
		this.lastCheckPointTime = lastCheckPointTime;
	}
	public String getTimeFromLeader() {
		return timeFromLeader;
	}
	public void setTimeFromLeader(String timeFromLeader) {
		this.timeFromLeader = timeFromLeader;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
}
    
    

