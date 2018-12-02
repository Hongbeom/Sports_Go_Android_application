package com.example.mcbback.sportgo;

import java.io.Serializable;

public class Facility implements Serializable {
    public String fac_name;
    public String fac_address;
    public String fac_image;
    public String fac_url;
    public String fac_info;
    public String fac_capacity;

    public Facility(){}

    public Facility(String name, String address, String image, String url, String info, String capacity){
        this.fac_name = name;
        this.fac_address = address;
        this.fac_image = image;
        this.fac_url = url;
        this.fac_info = info;
        this.fac_capacity = capacity;
    }
}
