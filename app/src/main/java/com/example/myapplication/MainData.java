package com.example.myapplication;

public class MainData {
    private String name_tv;
    private String number_tv;

    public MainData(String name_tv, String number_tv) {
        this.name_tv = name_tv;
        this.number_tv = number_tv;
    }

    public String getName_tv() {
        return name_tv;
    }

    public void setName_tv(String name_tv) {
        this.name_tv = name_tv;
    }

    public String getNumber_tv() {
        return number_tv;
    }

    public void setNumber_tv(String number_tv) {
        this.number_tv = number_tv;
    }
}
