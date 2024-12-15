
package application;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Race {
    private Track track;
    private ObservableList<Skier> skiers;
    private int milliseconds = 5000;
    private LocalTime localTime = LocalTime.of(00, 00, 00, 00);
    private int speedSimulator = 1;
    Skier leader; 
    static ObservableList<Skier> raceSeedingList = FXCollections.observableArrayList();

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

     void simulateRace() {
        while (!skiersFinished()) {
            UtilitySki.interval(getSpeedSimulator());
            addTime(getMilliseconds());
            for (Skier skier : getSkiers()) {
                skierAction(skier);
                Platform.runLater(() -> skier.updateTime());
            }
        }
        // -- New code 13/12/2024
        serializeSkiers(Main.skierList);
        ArrayList<SerializableSkier> deserializedSkiers = deseralizer(); // Deserializer method called. Returns an ArrayList of SerializableSkier objects from the .xml file and stores them in an ArrayList called "deserializedSkiers".
        bubbleSortSeededSkierList(conversionForTableView(deserializedSkiers));
    }
     // --
     
     // -- New code 13/12/2025 --
     // This method will convert the ArrayList from deserialization into an ObservableList that can be displayed in TableView.
    private ObservableList<Skier> conversionForTableView(ArrayList<SerializableSkier>  deserializedSkiers) {
		for (SerializableSkier skier : deserializedSkiers) {
			Skier seededSkier = new Skier();
			seededSkier.setName(skier.getName());
			seededSkier.setStartNumber(skier.getStartNumber());
			seededSkier.setDeserializedFinishTime(LocalTime.parse(skier.getFinishTime())); // Susceptible to fail if finishTime String contains a formatting error??
			raceSeedingList.add(seededSkier);		
			}
		return raceSeedingList;
	}
    // -- 
    
    // -- New code 13/12/2025 --
    // This method will bubble-sort the list according to finishing times.
    private void bubbleSortSeededSkierList(ObservableList<Skier> raceSeedingList) {
        for (int i = 0; i < raceSeedingList.size() - 1; i++) { 
            for (int j = 0; j < raceSeedingList.size() - i - 1; j++) { 
                if (raceSeedingList.get(j).getDeserializedFinishTime().isAfter(raceSeedingList.get(j + 1).getDeserializedFinishTime())) {
                    Skier temp = raceSeedingList.get(j);
                    raceSeedingList.set(j, raceSeedingList.get(j + 1));
                    raceSeedingList.set(j + 1, temp);
                }
            }
        }
        for (int i = 0; i < raceSeedingList.size(); i++) { // This will run after the list has been sorted and will assign the skiers a new sorting number.
        	raceSeedingList.get(i).setStartNumber(i+1);
        }
    }
    // --

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
            Main.resultsTable.refresh();
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
    
    // -- New Code 13/12/2024 --
    // A helper method to reformat a duration variable (stored within a SimpleObjetProperty wrapper) to a string in a readable format. -- 
    public String formatDuration(SimpleObjectProperty<Duration> simpleObjectProperty) {
    	Duration duration = simpleObjectProperty.get(); // This 'extracts' the Duration value from it's simpleObjectProperty wrapper.
    	long hours = duration.toHours();
    	long minutes = duration.toMinutes();
    	long seconds = duration.toSeconds();
    	return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    // --

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
    
    public void serializeSkiers(ObservableList<Skier> skierList) {
        try {
            ArrayList<SerializableSkier> skiers = new ArrayList<>();
            Serialize serialize = new Serialize();
            
            // Populate the skiers list with SerializableSkier objects
            for (Skier skier : skierList) {
                SerializableSkier serializeSkier = new SerializableSkier();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Ensures correct formatting of finish time.
                serializeSkier.setName(skier.getName());
                serializeSkier.setStartNumber(skier.getStartNumber());
                serializeSkier.setSpeed(skier.getSpeed());
                serializeSkier.setFinished(skier.isFinished());
                serializeSkier.setStartType(skier.getStartType());
                serializeSkier.setRaceDistance(skier.getRaceDistance());
                serializeSkier.setLastCheckPointTime(skier.getLastCheckPointTime().toString());
                serializeSkier.setTimeFromLeader(formatDuration(skier.getTimeFromLeader()));
                serializeSkier.setFinishTime(skier.getFinishTime().toString());
                serializeSkier.setDistance(skier.getDistance());
            
                skiers.add(serializeSkier);
            }
            serialize.encodeObject(skiers);
        } catch (IOException ex) {
            System.out.println("Something went wrong with serialization: " + ex.getMessage());
        }
    }

    public ArrayList<SerializableSkier> deseralizer() {
    	
    	try {
    		ArrayList<SerializableSkier> skiers;
    		Serialize serialize = new Serialize();
			skiers = serialize.decodeObject(getSkiers().size());
			return skiers;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    
    
    
    
    
}
