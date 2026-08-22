package com.kpimailserver;

public class KPI {


    private String date;
    private String hour;

    private int mailRelayed;
    private int spam;
    private int virus;

    public KPI(String date, String hour, int mailRelayed, int spam, int virus) {
        this.date = date;
        this.hour = hour;
        this.mailRelayed = mailRelayed;
        this.spam = spam;
        this.virus = virus;
    }


    public String getDate() {
        return date;
    }


    public String getHour() {
        return hour;
    }


    public int getMailRelayed() {
        return mailRelayed;
    }


    public int getSpam() {
        return spam;
    }


    public int getVirus() {
        return virus;
    }


}