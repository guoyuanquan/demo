package com.example.demo.model;

import lombok.Data;

@Data
public class RadarData {

    private String radarId;

    private String targetId;

    private short targetAmpliyude;

    private int targetSize;

    private float targetDistence;

    private float targetAngle;

    private float targetSpeed;

    private float targetCourse;

    private double longitude;

    private double latitude;

    private int targetQuality;

    private int targetSeaPropIntV;

    private String targetSeaProp;

    private int targetReliability;

    private int targetStabilityIntV;

    private String targetStability;

    private String timestamp;

    public void setRadarData(short radarId, int targetId,short targetAmpliyude,int targetSize,float targetDistence,float targetAngle,float targetSpeed,float targetCourse,double longitude,double latitude,int targetQuality,int targetSeaPropIntV, String targetSeaProp,int targetReliability,int targetStabilityIntV,String targetStability,String timestamp){
        this.radarId=String.valueOf(radarId);
        this.targetId=String.valueOf(targetId);
        this.targetAmpliyude=targetAmpliyude;
        this.targetSize=targetSize;
        this.targetDistence=targetDistence;
        this.targetAngle=targetAngle;
        this.targetSpeed=targetSpeed;
        this.targetCourse=targetCourse;
        this.longitude=longitude;
        this.latitude=latitude;
        this.targetQuality=targetQuality;
        this.targetSeaPropIntV=targetSeaPropIntV;
        this.targetSeaProp=targetSeaProp;
        this.targetReliability=targetReliability;
        this.targetStabilityIntV=targetStabilityIntV;
        this.targetStability=targetStability;
        this.timestamp=timestamp;

    }
}
