package com.example.demo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class FileAction {

    public static void main(String args[]){
        String path="C:\\Users\\admin\\Desktop\\bj\\30+200.txt";
        List<String> lines = FileUtil.readLines(path, Charset.forName("utf-8"));
        List<Integer[]> result = new ArrayList<>();
        List<Long> countInt = new ArrayList<>();
        List<Date> endList  = new ArrayList<>();
        List<Date> startList = new ArrayList<>();
        lines.forEach(l -> {
            if (StrUtil.isNotBlank(l)) {
                Date start = DateUtil.parse(StrUtil.subSuf(l.trim(), StrUtil.indexOf(l, 'A') + 1).trim(), "yyyy-MM-dd HH.mm.ss.SSS");
                Date end = DateUtil.parse(StrUtil.subPre(l.trim(), 23).trim(), "yyyy-MM-dd HH:mm:ss.SSS");
                Integer [] item = new Integer[2];
//                System.out.println(start+ "   "+end);
                item[0] = Integer.valueOf(StrUtil.subBetween(l, "+0800 ", "A"));
                item[1] = Long.valueOf(end.getTime() - start.getTime()).intValue();
                log.info("相差时间"+Long.valueOf(end.getTime() - start.getTime()));
                countInt.add(Long.valueOf(end.getTime() - start.getTime()));
                endList.add(end);
                startList.add(start);
                result.add(item);
            }
        });
        Date startTime = Collections.min(startList);
        Date endTime = Collections.max(endList);
        Long maxTime = Collections.max(countInt);
        Long minTime = Collections.min(countInt);
        Long countTime = 0L;
        for (int i=0;i<countInt.size();i++){
            countTime+=countInt.get(i);
        }
        log.info("最早时间："+startTime);
        log.info("最晚时间:"+endTime);
        log.info("最小时间"+minTime/1000+"秒");
        log.info("最大时间"+maxTime/1000+"秒");
        log.info("平均时间"+countTime/50+"毫秒");
        result.sort((a, b) -> a[0].compareTo(b[0]));
        System.out.println(JSONObject.toJSONString(result));
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
