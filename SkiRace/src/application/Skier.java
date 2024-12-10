package application;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Random;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.shape.Circle;

public class Skier implements Serializable, Ski {

    private static final long serialVersionUID = 1L;
    private String name;
    private int startNumber;
    private Timer timer = new Timer();
    private double speed = 0.0;
    private boolean finished = false;
    private Random random = new Random();
    private String raceType;
    private int raceDistance;
    private SimpleObjectProperty<LocalTime> lastMiddleTime = new SimpleObjectProperty<>(LocalTime.of(0, 0, 0));
    private SimpleStringProperty finishTime = new SimpleStringProperty("00:00:00:000");
    private SimpleIntegerProperty distance = new SimpleIntegerProperty(0);

    public Skier() {}

    public Skier(String name, String raceType, int startNumber, Timer timer, double distance, double speed, boolean finished) {
        this.name = name;
        this.startNumber = startNumber;
        this.timer = timer;
        this.speed = speed;
        this.finished = finished;
        this.raceType = raceType;
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
        setDistance((int)(getSpeed() * time) + getDistance());
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

    public void updateTime() {
        if (timer.getStartTime() != null && timer.getFinishTime() != null) {
            setFinishTime(timer.TimeBetweenStartAndFinish());
        } else {
            setFinishTime("N/A");
        }
    }
    
    // This method is needed to return a distance object that is observable for the TableView in Main.
    public SimpleIntegerProperty distanceProperty() {
    	return distance;
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

    public int getDistance() {
        return distance.get();
    }

    public void setDistance(int distance) {
    	this.distance.set(distance); 
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
        return lastMiddleTime.get();
    }

    public void setLastMiddleTime(LocalTime lastMiddleTime) {
        this.lastMiddleTime.set(lastMiddleTime);
    }

    public SimpleObjectProperty<LocalTime> lastMiddleTimeProperty() {
        return lastMiddleTime;
    }

    public String getFinishTime() {
        return finishTime.get();
    }

    public void setFinishTime(String currentTime) {
        this.finishTime.set(currentTime);
    }

    public SimpleStringProperty currentTimeProperty() {
        return finishTime;
    }
}
