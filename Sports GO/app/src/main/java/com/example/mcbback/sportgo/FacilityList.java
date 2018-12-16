package com.example.mcbback.sportgo;


import java.io.Serializable;

public class FacilityList implements Serializable {
    public String list_facilityName;
    public String list_facilityPicture;
    public String list_facilityAddress;
    public String list_facilityCapacity;
    public String list_facilityInfo;
    public String list_facility_url;





    public FacilityList(){

    }

    public FacilityList(String name, String picture, String address, String capacity, String info, String link){
        this.list_facilityName = name;
        this.list_facilityPicture = picture;
        this.list_facilityAddress = address;
        this.list_facilityCapacity = capacity;
        this.list_facilityInfo = info;
        this.list_facility_url = link;
    }




}

