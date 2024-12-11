package application;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

public class Race {
    private Track track;
    private ObservableList<Skier> skiers;
    private int milliseconds = 1000;
    private LocalTime localTime = LocalTime.of(00, 00, 00, 00);
    private int speedSimulator = 1;
    Skier leader; 

    public Race(Track track, ObservableList<Skier> skiers) {
        this.track = track;
        this.skiers = skiers;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ObservableList<Skier> getSkiers() {
        return skiers;
    }

    public void setSkiers(ObservableList<Skier> skiers) {
        this.skiers = skiers;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public int getSpeedSimulator() {
        return speedSimulator;
    }

    public void setSpeedSimulator(int speedSimulator) {
        this.speedSimulator = speedSimulator;
    }

    public void startRace() {
        Thread thread = new Thread(() -> {
            for (Skier skier : getSkiers()) {
                skier.getTimer().setStartTime(localTime);
                skier.start();
            }
            simulateRace();
        });
        thread.start();
    }

    private void simulateRace() {
        while (!skiersFinished()) {
            UtilitySki.interval(getSpeedSimulator());
            addTime(getMilliseconds());
            for (Skier skier : getSkiers()) {
                skierAction(skier);
                Platform.runLater(() -> skier.updateTime());
            }
        }
 
        serializeSkiers();
    }

    private void skierAction(Skier skier) {
        if (!skier.isFinished()) {
            skier.accelerateOrDecelerate();
            skier.distanceTraveled(getMilliseconds());
            checkCheckPointTime(skier);
            skierCrossedLine(skier);
        }
    }

    private void skierCrossedLine(Skier skier) {
        if (skier.getDistance() >= track.getDistance()) {
            skier.setFinished(true);
            skier.getTimer().setFinishTime(getLocalTime());
            skier.updateTime();
        }
    }

    public boolean skiersFinished() {
        for (Skier skier : getSkiers()) {
            if (!skier.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public void addTime(int milliseconds) {
        localTime = localTime.plusNanos(milliseconds * 1000000);
    }

    public void checkCheckPointTime(Skier skier) {
        int missingCheckPointTimes = track.getPhotoCells().size() - skier.getTimer().getCheckPointTimes().size();
        if (missingCheckPointTimes == 0) return;

        for (int i = skier.getTimer().getCheckPointTimes().size(); i < track.getPhotoCells().size(); i++) {
            if (skier.getDistance() >= track.getPhotoCells().get(i)) {
                addCheckPointTimeToSkier(skier, track.getPhotoCells().get(i));
                leader = Main.skierList.get(0);
                FindLeader();
                // Calculate the duration between the leader and the skier
                Duration timeDifference = Duration.between(leader.getLastCheckPointTime(), skier.getLastCheckPointTime());
                skier.setTimeFromLeader(timeDifference);
            } else {
                break; 
            }
        }
    }


    public void addCheckPointTimeToSkier(Skier skier, double distance) {
        if (skier.getDistance() >= distance ) {
            skier.getTimer().getCheckPointTimes().add(localTime);
            skier.setLastCheckPointTime(skier.getTimer().getCheckPointTimes().getLast());
        }
    }
    
    // -- Find Leader --
    public void FindLeader() {
    	for (Skier skier : Main.skierList) {
    		if (skier.getDistance() > leader.getDistance()) {
    			leader = skier;
    		}
    	}
    }
    
    public void serializeSkiers() {
		try {
			Serialize serialize = new Serialize();
			ArrayList<SerializableSkier> serializableSkiers = new ArrayList<>();
			
			for (Skier skier : getSkiers()) {
				SerializableSkier serializeSkier = new SerializableSkier();
				serializeSkier.setName(skier.getName());
				serializeSkier.setStartNumber(skier.getStartNumber());
				serializeSkier.setSpeed(skier.getSpeed());
				serializeSkier.setFinished(skier.isFinished());
				serializeSkier.setStartType(skier.getStartType());
				serializeSkier.setRaceDistance(skier.getRaceDistance());
				serializeSkier.setLastCheckPointTime(skier.getLastCheckPointTime().toString());
				serializeSkier.setTimeFromLeader(skier.getTimeFromLeader().toString());
				serializeSkier.setFinishTime(skier.getFinishTime().toString());
				serializeSkier.setDistance(skier.getDistance());
				serializableSkiers.add(serializeSkier);
			}
			
			serialize.encoder(serializableSkiers);
			serialize.getXmlEncoder().close();
		} catch(IOException ex) {
			System.out.println("Something went wrong with serialization" + ex.getMessage());
		}
    }
    
}
