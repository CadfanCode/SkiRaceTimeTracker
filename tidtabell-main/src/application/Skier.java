package application;

import java.io.Serializable;
import java.util.Random;

import javafx.scene.shape.Circle;

public class Skier extends Person implements Serializable, Ski {

	private static final long serialVersionUID = 1L;
	private int startNumber;
	private Timer timer = new Timer();
	private double distance = 0;
	private double speed = 0;
	private boolean finished = false;
	private Random random = new Random(); 
	
	public Skier() {}

	public Skier(String name, int startNumber, Timer timer, double distance, double speed, boolean finished) {
		super(name);
		this.startNumber = startNumber;
		this.timer = timer;
		this.distance = distance;
		this.speed = speed;
		this.finished = finished;
	}
	
	public Skier(String name, int startNumber) {
		super(name);
		this.startNumber = startNumber;
	}

	@Override
	public void start() {		
		for (int i = 0; i < 5; i++) {
			accelerate();
		}
		distanceTraveled(1000);
	}
	
	@Override
	public void stop() {
		setSpeed(0);
		setFinished(true);
	}
	
	public void distanceTraveled(int milliSeconds) {
		double time = milliSeconds * 0.001;
		setDistance((getSpeed() * time) + getDistance());
	}

	@Override
	public void accelerate() {
		double currentSpeed = getSpeed() + (random.nextDouble() * 3);
		setSpeed(Math.min(currentSpeed, 30));
	}
	
	@Override
	public void decelerate() {
		double currentSpeed = getSpeed();
		currentSpeed = currentSpeed - (random.nextDouble() * 3);
		setSpeed(Math.max(currentSpeed, 3));
	}
	
	public void accelerateOrDecelerate() {
		if (random.nextBoolean()) {
			accelerate();
		} else {
			decelerate();
		}
	}
	
    public Circle skierMarker() {
        Circle skier = new Circle();
        skier.setId(String.valueOf(getStartNumber()));
        skier.setRadius(5);
        skier.setTranslateX(getDistance());
        skier.setTranslateY(100);
        return skier;
    }
    
    public String getFinishTime() {    	
    		return getName() + ": Gick i mål på tiden: " + getTimer().TimeBetweenStartAndFinish();
    }
	
	public int getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(int startNumber) {
		this.startNumber = startNumber;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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
	
}
