package application;

import java.time.LocalTime;
import javafx.collections.ObservableList;

public class Race {
	private Track track;
	private ObservableList<Skier> skiers;
	private int milliseconds = 10;
	private LocalTime localTime = LocalTime.of(00, 00, 00, 00);
	private int speedSimulator = 1; 
	
	public Race() {}

	public Race(Track track, ObservableList<Skier> skiers) {
		this.track = track;
		this.skiers = skiers;
	}
	
	public Race(Track track, ObservableList<Skier> skiers, int milliseconds) {
		this.track = track;
		this.skiers = skiers;
		this.milliseconds = milliseconds;
	}
	
	public Race(Track track, ObservableList<Skier> skiers, int milliseconds, LocalTime localTime) {
		this.track = track;
		this.skiers = skiers;
		this.milliseconds = milliseconds;
		this.localTime = localTime;
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
		while(!skiersFinished()) {
			UtilitySki.interval(getSpeedSimulator());
			addTime(getMilliseconds());
			for (Skier skier : getSkiers()) {
				skierAction(skier);
			}
		}		
	};
	
	private void skierAction(Skier skier) {
		if (!skier.isFinished()) {
			skier.accelerateOrDecelerate();
			skier.distanceTraveled(getMilliseconds());
			checkMiddleTime(skier);
			skierCrossedLine(skier);
		}
	}
	
	private void skierCrossedLine(Skier skier) {
		if (skier.getDistance() >= track.getDistance()) {
			skier.setFinished(true);
			skier.getTimer().setFinishTime(getLocalTime());
		}
	}
	
	public boolean skiersFinished() {
		for (Skier skier: getSkiers()) {
			if(!skier.isFinished()) {
				return false;
			}
		}
		return true;
	}

	public void addTime(int milliseconds) {
		LocalTime time = getLocalTime();
		time = time.plusNanos(milliseconds * 1000000);
		setLocalTime(time);
	}
	
	public void checkMiddleTime(Skier skier) {
	    int missingMiddleTimes = track.getPhotoCells().size() - skier.getTimer().getMiddleTimes().size();
	    
	    if (missingMiddleTimes == 0) {
	        return;
	    }

	    for (int i = 0; i < track.getPhotoCells().size(); i++) {
	        if (i == skier.getTimer().getMiddleTimes().size()) {
	            addMiddleTimeToSkier(skier, track.getPhotoCells().get(i));
	            break;
	        }
	    }
	}

	public void addMiddleTimeToSkier(Skier skier, double distance) {
	    if (skier.getDistance() >= distance) {
	    		
	        skier.getTimer().getMiddleTimes().add(getLocalTime());
	        skier.setLastMiddleTime(skier.getTimer().getMiddleTimes().getLast());	       
	    }
	}
}
