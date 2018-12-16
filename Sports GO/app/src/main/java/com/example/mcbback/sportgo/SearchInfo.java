package com.example.mcbback.sportgo;

import java.io.Serializable;

public class SearchInfo implements Serializable {
    public String sports;
    public String city;
    public String gu;
    public String date;

    public SearchInfo(){

    }
    public SearchInfo(String sports, String city, String gu, String date){
        this.sports = sports;
        this.city = city;
        this.gu = gu;
        this.date = date;
    }
}
