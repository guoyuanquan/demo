package com.example.demo.model;

import lombok.Data;

@Data
public class DynamicReceive {
    private String userid;

    private String receive_time;

    private int messageid;

    private String nav_status;

    private double rot;

    private double sog;

    private double longitude;

    private double latitude;

    private double cog;

    private double true_heading;

    private int dsource;
}
