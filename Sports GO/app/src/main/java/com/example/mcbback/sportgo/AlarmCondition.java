package com.example.mcbback.sportgo;

import java.io.Serializable;

public class AlarmCondition implements Serializable {
    public int condition;
    public AlarmCondition(){

    }
    public AlarmCondition(int condition){
        this.condition = condition;
    }

}
