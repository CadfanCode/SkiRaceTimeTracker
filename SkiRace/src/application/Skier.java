package application;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Random;
import javafx.scene.shape.Circle;

public class Skier implements Serializable, Ski {

    private static final long serialVersionUID = 1L;
    private String name;
    private int startNumber;
    private Timer timer = new Timer();
    private double distance = 0;
    private double speed = 0;
    private boolean finished = false;
    private Random random = new Random(); 
    private String raceType; // Added to store raceType
    private int raceDistance; // Add to store raceDistance
    private LocalTime lastMiddleTime; // This is needed for use in the TableView.
    private String currentTime; // This is used to store the current time as a string for TableView.
    
    public Skier() {}

    public Skier(String name, String raceType, int startNumber, Timer timer, double distance, double speed, boolean finished) {
        this.name = name;
        this.startNumber = startNumber;
        this.timer = timer;
        this.distance = distance;
        this.speed = speed;
        this.finished = finished;
        this.raceType = raceType;
        this.lastMiddleTime = LocalTime.of(0, 0, 0);
    }

    public Skier(String name, String raceType, int raceDistance, int startNumber) {
        this.name = name;
        this.startNumber = startNumber;
        this.raceType = raceType;
        this.raceDistance = raceDistance;
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

    public void updateTime() {
        if (timer.getStartTime() != null && timer.getFinishTime() != null) {
            String time = timer.TimeBetweenStartAndFinish();  // Get the formatted time string
            setCurrentTime(time);  // Set the new current time
        } else {
            setCurrentTime("N/A");  // Handle when the time isn't available
        }
    }

    // Getters and Setters
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

    public String getRaceType() {
        return raceType;
    }

    public void setRaceType(String raceType) {
        this.raceType = raceType;
    }

    public int getRaceDistance() {
        return raceDistance;
    }

    public void setRaceDistance(int raceDistance) {
        this.raceDistance = raceDistance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getLastMiddleTime() {
        return lastMiddleTime;
    }

    public void setLastMiddleTime(LocalTime middleTime) {
        this.lastMiddleTime = middleTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
