package com.example.demo.model;

import lombok.Data;

@Data
public class ImageExifDto {
    private Double longitude;

    private Double latitude;

    private Double altitude;

    private String dateTime;

    private String url;

    private String absolutePath;

    private String fileName;
}
