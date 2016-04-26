package com.udl.monitorizacion.backend;


/**********
 *
 **********/
/** The object model for the data we are sending through endpoints */
public class Location {

    private Long id;
    private Double latitude;
    private String date;
    private String time;
    private Double longitude;



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public Double getLongitude() {
        return longitude;
    }


    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




}