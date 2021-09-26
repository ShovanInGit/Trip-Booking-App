package com.example.escape.model;

public class BookedPost {

    private String BookingSeat,OrganizerUid,PostKey,PostTitle,PricePerPerson,TotalPrice,TrsId,UserPhone;

    public BookedPost() {
    }

    public BookedPost(String bookingSeat, String organizerUid, String postKey, String postTitle, String pricePerPerson, String totalPrice, String trsId, String userPhone) {
        BookingSeat = bookingSeat;
        OrganizerUid = organizerUid;
        PostKey = postKey;
        PostTitle = postTitle;
        PricePerPerson = pricePerPerson;
        TotalPrice = totalPrice;
        TrsId = trsId;
        UserPhone = userPhone;
    }

    public String getBookingSeat() {
        return BookingSeat;
    }

    public void setBookingSeat(String bookingSeat) {
        BookingSeat = bookingSeat;
    }

    public String getOrganizerUid() {
        return OrganizerUid;
    }

    public void setOrganizerUid(String organizerUid) {
        OrganizerUid = organizerUid;
    }

    public String getPostKey() {
        return PostKey;
    }

    public void setPostKey(String postKey) {
        PostKey = postKey;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public String getPricePerPerson() {
        return PricePerPerson;
    }

    public void setPricePerPerson(String pricePerPerson) {
        PricePerPerson = pricePerPerson;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getTrsId() {
        return TrsId;
    }

    public void setTrsId(String trsId) {
        TrsId = trsId;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
