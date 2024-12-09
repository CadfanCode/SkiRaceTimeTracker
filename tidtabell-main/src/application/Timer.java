package application;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Timer {
	private LocalTime startTime;
	private LocalTime finishTime;
	private ArrayList<LocalTime> middleTimes = new ArrayList<>(); 

	public Timer() {}

	public Timer(LocalTime startTime, LocalTime finishTime) {
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
	
	public String TimeBetweenStartAndFinish() {
	    Duration duration = Duration.between(getStartTime(), getFinishTime());
	    long millis = duration.toMillis();
	    
	    long hours = millis / 3_600_000;
	    millis %= 3_600_000;
	    long minutes = millis / 60_000;
	    millis %= 60_000;
	    long seconds = millis / 1_000;
	    millis %= 1_000;

	    return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, millis);
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}

	public ArrayList<LocalTime> getMiddleTimes() {
		return middleTimes;
	}

	public void setMiddleTimes(ArrayList<LocalTime> middleTimes) {
		this.middleTimes = middleTimes;
	}
	
}
