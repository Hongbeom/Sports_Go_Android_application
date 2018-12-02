package com.example.mcbback.sportgo;


public class UserList {
    public String list_userName;
    public String list_userEmail;
    public String list_userSex;
    public String list_userReliability;
    public String list_userCity;
    public String list_userGu;




    public UserList(){

    }

    public UserList(String name, String email, String sex, String reliability, String city, String gu){
        this.list_userName = name;
        this.list_userEmail = email;
        this.list_userSex = sex;
        this.list_userReliability = reliability;
        this.list_userCity = city;
        this.list_userGu = gu;

    }




}
