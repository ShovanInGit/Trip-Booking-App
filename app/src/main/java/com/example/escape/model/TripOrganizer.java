package com.example.escape.model;

public class TripOrganizer {

    private String  email,fullname,phoneno,licenceno,password,frontnid,backnid;

    public TripOrganizer() {
    }

    public TripOrganizer(String email, String fullname, String phoneno, String licenceno, String password, String frontnid, String backnid) {
        this.email = email;
        this.fullname = fullname;
        this.phoneno = phoneno;
        this.licenceno = licenceno;
        this.password = password;
        this.frontnid = frontnid;
        this.backnid = backnid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getLicenceno() {
        return licenceno;
    }

    public void setLicenceno(String licenceno) {
        this.licenceno = licenceno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrontnid() {
        return frontnid;
    }

    public void setFrontnid(String frontnid) {
        this.frontnid = frontnid;
    }

    public String getBacknid() {
        return backnid;
    }

    public void setBacknid(String backnid) {
        this.backnid = backnid;
    }
}
