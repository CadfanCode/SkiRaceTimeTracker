package application;

public class Athlete {
// Attributes
	private String name;
	private String startType;
	private int raceDistance;
	private double startTime;
	private double finishTime;
	private int distanceTravelled;
	private double timeFromLeader;
	private double timeElapsed;
	// Empty constructor
	public Athlete() {
		
	}
	
	// Constructor with parameters
	public Athlete(String name, String startType, int raceDistance) {
		this.name = name;
		this.startType = startType;
		this.raceDistance = raceDistance;
		this.startTime = -1;
		this.finishTime = -1;
		this.distanceTravelled = -1;
		this.timeFromLeader = -1;
		this.timeElapsed = -1;
	}

	public String getName() {
		return name;
	}

	public String getStartType() {
		return startType;
	}

	public int getRaceDistance() {
		return raceDistance;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getFinishTime() {
		return finishTime;
	}

	public int getDistanceTravelled() {
		return distanceTravelled;
	}

	public double getTimeFromLeader() {
		return timeFromLeader;
	}

	public double getTimeElapsed() {
		return timeElapsed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}

	public void setRaceDistance(int raceDistance) {
		this.raceDistance = raceDistance;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}

	public void setDistanceTravelled(int distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}

	public void setTimeFromLeader(double timeFromLeader) {
		this.timeFromLeader = timeFromLeader;
	}

	public void setTimeElapsed(double timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

}
	
