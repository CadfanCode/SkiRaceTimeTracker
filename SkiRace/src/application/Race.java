
package application;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Race {
    private Track track;
    private ObservableList<Skier> skiers;
    private int milliseconds = 1000;
    private LocalTime localTime = LocalTime.of(00, 00, 00, 00);
    private int speedSimulator = 10;
    private Skier leader; 
    static ObservableList<Skier> raceSeedingList = FXCollections.observableArrayList();
    static ArrayList<SerializableSkier> deserializedSkiers;
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
    
    public Skier getLeader() {
        return leader;
    }

    public void setLeader(Skier leader) {
        this.leader = leader;
    }

    public void startRace() {
    	
    	
    	
        Thread thread = new Thread(() -> {
            for (Skier skier : getSkiers()) {
                if(skier.getStartType().equals("jaktStart")) {
                	updateLeaderJaktStart();
                }
                skier.start();
            }
            simulateRace();
        });
        thread.start();
    }
    
    void simulateRace() {
        while (!skiersFinished()) {
            UtilitySki.interval(getSpeedSimulator()); // Tidsintervall för simuleringen
            addTime(getMilliseconds()); // Lägg till tid för loppet

            for (Skier skier : getSkiers()) {
                if (skier.getTimer().getStartTime().isBefore(getLocalTime())) {
                    skierAction(skier); // Uppdatera deltagare

                    Platform.runLater(() -> {
                        skier.updateTime(); // Uppdatera skidåkarens tid

                        // Uppdatera ledaren baserat på distans och måltid

//                        	updateLeader();                        	
//                        }
//                        
                        // Beräkna tidsskillnad mellan den aktuella ledaren och deltagare
//                        calculateTimeFromLeader(skier);
                    });
                }
            }
        }

        // Efter loppet: serialisera deltagare och lagra resultat
        serializeSkiers(Main.skierList);
        deserializedSkiers = deseralizer();
        bubbleSortSeededSkierList(conversionForTableView(deserializedSkiers));
    }
    
    
    
    private void updateLeader() {
        for (Skier skier : getSkiers()) {
            if (leader == null || 
                (skier.getDistance() > leader.getDistance() && !skier.isFinished()) || 
                (skier.getFinishTime() != null && 
                 (leader.getFinishTime() == null || skier.getFinishTime().isBefore(leader.getFinishTime())))) {
                leader = skier; // Uppdatera ledaren
            }
        }
    }
    
    private void updateLeaderJaktStart() {
    	ArrayList<Skier> skiers = new ArrayList<>();
    	skiers.addAll(getSkiers());
    	skiers.sort(Comparator.comparing(Skier::getDistance));
    	setLeader(skiers.getLast());
    }
    
    private void calculateTimeFromLeaderJaktStart(Skier skier) {    	
    	try {
    		int photoCellNumber = skier.getTimer().getCheckPointTimes().size() - 1;
    		LocalTime skierTime = skier.getTimer().getCheckPointTimes().get(photoCellNumber);
    		LocalTime leaderTime = leader.getTimer().getCheckPointTimes().get(photoCellNumber);
    		Duration timeDifference = Duration.between(leaderTime, skierTime);
    		Duration adjustedTimeDifference = timeDifference.plus(Duration.between(LocalTime.MIN, skier.getStartTime()));
    		
    		
    		skier.setTimeFromLeader(adjustedTimeDifference);    		
    	} catch (IndexOutOfBoundsException ex) {    		
    	}
    }
    
    private void calculateTimeFromLeader(Skier skier) {
        if (leader != null) {
            Duration timeDifference;

            if (skier.getFinishTime() != null) {
                // Om deltagare har gått i mål
                timeDifference = Duration.between(leader.getFinishTime(), skier.getFinishTime());
            } else if (leader.getLastCheckPointTime() != null && skier.getLastCheckPointTime() != null) {
                // Om både ledaren och motståndare är vid checkpoint
                timeDifference = Duration.between(leader.getLastCheckPointTime(), skier.getLastCheckPointTime());
            } else {
                // Om det inte finns giltiga tider
                timeDifference = Duration.ZERO;
            }

            // Omvandlar negativ tidsskillnad till absolut värde
            skier.setTimeFromLeader(timeDifference.abs());
        } else {
            skier.setTimeFromLeader(Duration.ZERO);
        }
    }
    
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
                                
                if(skier.getStartType().equals("jaktStart")) {
                	updateLeaderJaktStart();
                	calculateTimeFromLeaderJaktStart(skier);
                	return;
                } else {
                	updateLeader();                        	
                }
                             
                Duration timeDifference = Duration.between(leader.getLastCheckPointTime(), skier.getLastCheckPointTime()).abs();
                skier.setTimeFromLeader(timeDifference);
            } else {
                break;
            }
        }
    }
    
    // -- New Code 13/12/2024 --
    // A helper method to reformat a duration variable (stored within a SimpleObjetProperty wrapper) to a string in a readable format. -- 
    public String formatDuration(SimpleObjectProperty<Duration> simpleObjectProperty) {
        if (simpleObjectProperty == null || simpleObjectProperty.get() == null) {
            return "00:00:00"; // Säkerhetskontroll för att undvika fel
        }
        Duration duration = simpleObjectProperty.get();
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart(); // Separerar minuter
        long seconds = duration.toSecondsPart(); // Separerar sekunder
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    
    public String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    
      
    public void addCheckPointTimeToSkier(Skier skier, double distance) {
        if (skier.getDistance() >= distance) {
            skier.getTimer().getCheckPointTimes().add(localTime);
            skier.setLastCheckPointTime(skier.getTimer().getCheckPointTimes().getLast());


        } else {
            System.out.println("Skier: " + skier.getName() + " has not reached distance: " + distance);
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
            
            // Hitta den aktuella ledaren (skidåkaren med den snabbaste sluttiden)
            Skier leader = null;
            for (Skier skier : skierList) {
                if (skier.getFinishTime() != null) {
                    if (leader == null || skier.getFinishTime().isBefore(leader.getFinishTime())) {
                        leader = skier;
                    }
                }
            }

            // Populate the skiers list with SerializableSkier objects
            for (Skier skier : skierList) {
                SerializableSkier serializeSkier = new SerializableSkier();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                // Sätt vanliga attribut
                serializeSkier.setName(skier.getName());
                serializeSkier.setStartNumber(skier.getStartNumber());
                serializeSkier.setStartTime(skier.getTimer().getStartTime().toString());
                serializeSkier.setSpeed(skier.getSpeed());
                serializeSkier.setFinished(skier.isFinished());
                serializeSkier.setStartType(skier.getStartType());
                serializeSkier.setRaceDistance(skier.getRaceDistance());
                serializeSkier.setLastCheckPointTime(skier.getLastCheckPointTime().toString());
                serializeSkier.setFinishTime(skier.getFinishTime() != null ? skier.getFinishTime().toString() : "N/A");
                serializeSkier.setDistance(skier.getDistance());

                // Beräkna och sätt timeFromLeader
                if (leader != null && leader.getFinishTime() != null && skier.getFinishTime() != null) {
                    Duration timeDifference = Duration.between(leader.getFinishTime(), skier.getFinishTime());
                    skier.setTimeFromLeader(timeDifference); // Uppdatera skidåkarens timeFromLeader
                    serializeSkier.setTimeFromLeader(formatDuration(timeDifference));
                } else {
                    serializeSkier.setTimeFromLeader("N/A"); // Om det saknas data
                }
                
                skiers.add(serializeSkier);
            }

            // Serialisera listan av skidåkare
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
    
    public void resetRace() {
    	leader = null;
		setLocalTime(LocalTime.of(00, 00, 00));
    }
    
    
}
