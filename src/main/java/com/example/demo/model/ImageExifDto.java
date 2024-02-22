package com.example.demo.model;

import lombok.Data;

@Data
public class ImageExifDto {
    private double longitude;

    private double latitude;

    private double altitude;

    private String dateTime;

    private String url;

    private String absolutePath;

    private String fileName;

    private String location;

    private String deptArea;
}
