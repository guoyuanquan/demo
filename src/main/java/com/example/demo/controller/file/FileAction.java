package com.example.demo.controller.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class FileAction {

    public static void main(String args[]){
        String path="C:\\Users\\admin\\Desktop\\nohup.out";
        List<String> lines = FileUtil.readLines(path, Charset.forName("utf-8"));
        JSONObject resultData = JSON.parseObject(lines.get(0));
        JSONArray shipInfoData = JSON.parseArray(resultData.getString("data"));
        for (int n=0;n<shipInfoData.size();n++){
            String shipInfo = shipInfoData.get(n).toString();
            JSONObject object = JSON.parseObject(shipInfo);
            Map beidou = JSONObject.parseObject(shipInfo,Map.class);

            //添加一批数据的标识
            beidou.put("tags","tags");
            log.info("船舶信息："+beidou);
        }
    }
}
/*
       （二）生成图表
        https://echarts.apache.org/examples/zh/editor.html?c=line-simple
        ====以下代码后，更换data数据====
        option = {
        xAxis: {
        name: '文件顺序',
        type: 'value'
        },
        yAxis: {
        name: '边界耗时(毫秒)',
        type: 'value'
        },
        series: [
        {
        data: [[1,20], [2,30]],
        type: 'line'
        }
        ]
        };
*/
