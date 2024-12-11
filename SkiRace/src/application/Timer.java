package application;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Timer {
    private LocalTime startTime;
    private LocalTime finishTime;
    private ArrayList<LocalTime> CheckPointTimes = new ArrayList<>();
    private String formattedTime;

    public Timer() {}

    public Timer(LocalTime startTime, LocalTime finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public String TimeBetweenStartAndFinish() {
        if (startTime != null && finishTime != null) {
            Duration duration = Duration.between(startTime, finishTime);
            long millis = duration.toMillis();
            long hours = millis / 3_600_000;
            millis %= 3_600_000;
            long minutes = millis / 60_000;
            millis %= 60_000;
            long seconds = millis / 1_000;
            millis %= 1_000;
            formattedTime = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, millis);
            return formattedTime;
        } else {
            return "N/A";
        }
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

    public ArrayList<LocalTime> getCheckPointTimes() {
        return CheckPointTimes;
    }

    public void setCheckPointTimes(ArrayList<LocalTime> CheckPointTimes) {
        this.CheckPointTimes = CheckPointTimes;
    }
    
    // Getter for formattedTime
    public String getFormattedTime() {
        return formattedTime;
    }

    // Setter for formattedTime
    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }
}
