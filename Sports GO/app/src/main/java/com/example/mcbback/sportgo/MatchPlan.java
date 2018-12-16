package com.example.mcbback.sportgo;

import java.io.Serializable;

public class MatchPlan implements Serializable {
    public String date;
    public String time;
    public String comment;
    public String address;

    public MatchPlan(){}

    public MatchPlan(String date, String time, String comment, String address){
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.address = address;
    }
}
