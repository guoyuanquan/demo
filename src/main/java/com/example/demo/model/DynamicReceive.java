package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("stg_universe_ais_gj_dt")
public class DynamicReceive {
    private String userid;

    private String receive_time;

    private String messageid;

    private String nav_status;

    private String rot;

    private String sog;

    private String longitude;

    private String latitude;

    private String cog;

    private String true_heading;

    private String dsource;

    private String uuid;
}
