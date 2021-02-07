package com.example.commoncalendar;

public class mainData {
    private String event;
    private String hours;

    public mainData(String event, String hours) {
        this.event = event;
        this.hours = hours;
    }

    public String getEvent() {
        return event;
    }

    public mainData setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getHours() {
        return hours;
    }

    public mainData setHours(String hours) {
        this.hours = hours;
        return this;
    }

    @Override
    public String toString() {
        return event + "!" + hours;
    }
}
