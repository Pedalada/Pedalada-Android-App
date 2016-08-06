package pedalada.pedalapp;

import java.util.ArrayList;

// a helper class to directly
public class Ride {

    private String username;
    private long startTime;
    private long finishTime;
    private ArrayList<Double[]> route;
    private String snapshot; // a bitmap coded in string

    public Ride() {

    }

    public Ride(String rider, long startTime) {
        this.username = rider;
        this.startTime = startTime;
    }

    public Ride(String rider, long startTime, long finishTime, ArrayList<Double[]> record, String snapshot) {
        this.username = rider;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.route = record;
        this.snapshot = snapshot;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public void setRouteRecord(ArrayList<Double[]> record) {
        this.route = record;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getUsername() {
        return username;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public ArrayList<Double[]> getRouteRecord() {
        return route;
    }

    public String getSnapshot() {
        return snapshot;
    }
}
