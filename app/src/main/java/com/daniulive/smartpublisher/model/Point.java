package com.daniulive.smartpublisher.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by flny on 2018/3/30.
 */

@Entity
public class Point {
    @Id(autoincrement = true)
    private Long id;
    private String userID;
    private Double latitude;
    private Double longitude;
    private Date date;
    @Generated(hash = 2088498519)
    public Point(Long id, String userID, Double latitude, Double longitude,
                 Date date) {
        this.id = id;
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }
    @Generated(hash = 1977038299)
    public Point() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserID() {
        return this.userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public Double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

}
