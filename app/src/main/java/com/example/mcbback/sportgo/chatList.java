package com.example.mcbback.sportgo;


public class chatList {

    private String id;
    private String content;
    private String time;

    public chatList(){}

    public chatList( String id, String content, String time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}