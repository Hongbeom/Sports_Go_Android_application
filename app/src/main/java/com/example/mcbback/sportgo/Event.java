package com.example.mcbback.sportgo;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    public String eventName;
    public String sports;
    public String city;
    public String gu;
    public String date;
    public String holder;
    public ArrayList<String> users = new ArrayList<String>();
    public MatchPlan matchPlan;

    public Event(){

    }
    public Event(String eventName, String sports, String city, String gu, String date, String holder){
        this.eventName = eventName;
        this.sports = sports;
        this.city = city;
        this.gu = gu;
        this.date = date;
        this.holder = holder;
        this.matchPlan = null;
    }

    public void joining(String uid){
        this.users.add(uid);
    }

    public void deletePlayer(String email){
        this.users.remove(users.indexOf(email));
    }

    public void setMatchPlan(String date, String time, String comment, String address){
        this.matchPlan = new MatchPlan(date, time, comment, address);
    }
}
