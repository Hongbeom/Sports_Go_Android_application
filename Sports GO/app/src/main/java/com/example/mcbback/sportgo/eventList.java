package com.example.mcbback.sportgo;


public class eventList {
    public String eventName;
    public String eventSports;
    public String eventDate;
    public String eventCapacity;
    public String holder;


    public void setEventName(String e_name) {
        eventName = e_name;
    }

    public void setEventSports(String e_Sports) {
        eventSports = e_Sports;
    }

    public void setEventDate(String e_Date) {
        eventDate = e_Date;
    }


    public String getEventName() {

        return this.eventName;
    }

    public String getEventSports() {
        return this.eventSports;
    }

    public String getEventDate() {
        return this.eventDate;
    }
    public eventList(){

    }

    public eventList(String eventName, String eventSports, String eventDate, String holder, String capacity){
        this.eventName = eventName;
        this.eventSports = eventSports;
        this.eventDate = eventDate;
        this.holder = holder;
        this.eventCapacity = capacity;
    }




}
