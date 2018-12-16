package com.example.mcbback.sportgo;

import java.io.Serializable;

public class Profile implements Serializable {
    public String email;
    public String name;
    public String sex;
    public String phone;
    public String city;
    public String gu;
    public String eventInfo;
    public String con_sports;
    public String con_city;
    public String con_gu;
    public float reliability;
    public float eval_num;
    public Profile(){

    }
    public Profile(String email, String name, String sex, String phone, String city, String gu, String eventInfo){
        this.email = email;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.city = city;
        this.gu = gu;
        this.eventInfo = eventInfo;
        this.reliability = 0;
        this.eval_num = 0;
    }
    public void setEventInfo(String eventInfo){
        this.eventInfo = eventInfo;
    }
    public void eval(float score){
        this.eval_num+=1;
        this.reliability = ((reliability*(eval_num-1))+score)/eval_num;
    }

    public void setCondtion(String sports, String city, String gu){
        this.con_sports = sports;
        this.con_city = city;
        this.con_gu = gu;
    }
}
