package com.example.escape.model;

public class OrganizerPost {

    private String Title,PostImage,Description,Time,Date,OrganizerKey,TotalSeat,AvailableSeat,Caution,ReturnTD,StartTD,PricePerPerson;

    public OrganizerPost() {
    }

    public OrganizerPost(String title, String postImage, String description, String time, String date, String organizerKey, String totalSeat, String availableSeat, String caution, String returnTD, String startTD, String pricePerPerson) {
        Title = title;
        PostImage = postImage;
        Description = description;
        Time = time;
        Date = date;
        OrganizerKey = organizerKey;
        TotalSeat = totalSeat;
        AvailableSeat = availableSeat;
        Caution = caution;
        ReturnTD = returnTD;
        StartTD = startTD;
        PricePerPerson = pricePerPerson;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrganizerKey() {
        return OrganizerKey;
    }

    public void setOrganizerKey(String organizerKey) {
        OrganizerKey = organizerKey;
    }

    public String getTotalSeat() {
        return TotalSeat;
    }

    public void setTotalSeat(String totalSeat) {
        TotalSeat = totalSeat;
    }

    public String getAvailableSeat() {
        return AvailableSeat;
    }

    public void setAvailableSeat(String availableSeat) {
        AvailableSeat = availableSeat;
    }

    public String getCaution() {
        return Caution;
    }

    public void setCaution(String caution) {
        Caution = caution;
    }

    public String getReturnTD() {
        return ReturnTD;
    }

    public void setReturnTD(String returnTD) {
        ReturnTD = returnTD;
    }

    public String getStartTD() {
        return StartTD;
    }

    public void setStartTD(String startTD) {
        StartTD = startTD;
    }

    public String getPricePerPerson() {
        return PricePerPerson;
    }

    public void setPricePerPerson(String pricePerPerson) {
        PricePerPerson = pricePerPerson;
    }
}
