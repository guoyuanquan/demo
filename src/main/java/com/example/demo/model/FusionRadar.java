package com.example.demo.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//接收对象
//雷达融合数据表

@Data
public class FusionRadar implements Serializable {
    private static final long serialVersionUID = 1L;

    //融合目标批号
    private  String fusionBatchNum;
    //采集时间  标准UTC时间
    private String collectTime;
    //目标经度 精度为0.000001度
    private double longitude;
    //目标纬度 精度为0.000001度
    private double latitude;
    //目标大小
    private double targetSize;
    //目标航向  单位度
    private double course;
    //目标航速  单位节
    private double speed;
    //融合时间  单位秒，UTC时间
    private String fusionTime;
    //目标航迹状态  ‘T’：稳定航迹；‘L’：航迹丢失（消批新增字段）
    private String traceState;
    //雷达编号
    private Integer radarCode;
    //融合源个数
    private Integer fusionSourceCount;
    //雷达批号
    private Integer radarBatchNum;
    //目标可信度  单位%
    private double reliability;
    //目标高度  单位m，目标海平面高度
    private float altitude;

    private Date receiveTime;

    private String trackId;
}
