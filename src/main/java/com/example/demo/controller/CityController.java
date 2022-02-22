package com.example.demo.controller;

import com.example.demo.service.IGeoService;
import com.example.demo.service.IRedisService;
import com.example.demo.service.IZsetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.lang.Thread.sleep;

@Slf4j
//@Component
public class CityController {


    @Autowired
    private IGeoService geoService;
    @Autowired
    private IZsetService zsetService;
    @Autowired
    private IRedisService redisService;


//    @Scheduled(initialDelay=6000, fixedRate=5000)
    public void remove(){
        log.info("start remove.");
        log.info(String.valueOf(zsetService.remove()));
    }

//    @PostConstruct
    public void distance(){
        System.out.println( geoService.getTwoCityDistance("two","three", Metrics.KILOMETERS));
    }

//    @Scheduled(cron = "*/5 * * * * ?")
    public void getPointRadius(){
        Point center1 = new Point(109.9385403535156, 18.912063808435725);
        Distance radius1 = new Distance(200, Metrics.KILOMETERS);
        Circle within1 = new Circle(center1, radius1);
        System.out.println("第一个:"+geoService.getPointRadius(within1,null));
        Point center2 = new Point(48.9385403535156, 20.912063808435725);
        Distance radius2 = new Distance(1200, Metrics.KILOMETERS);
        Circle within2 = new Circle(center2, radius2);
        System.out.println("第二个:"+geoService.getPointRadius(within2,null));
        Point center3 = new Point(32.9385403535156, 9.912063808435725);
        Distance radius3 = new Distance(1200, Metrics.KILOMETERS);
        Circle within3 = new Circle(center3, radius3);
        System.out.println("第三个:"+geoService.getPointRadius(within3,null));
        Point center4 = new Point(65.9385403535156, 1.912063808435725);
        Distance radius4 = new Distance(1200, Metrics.KILOMETERS);
        Circle within4 = new Circle(center4, radius4);
        System.out.println("第四个:"+geoService.getPointRadius(within4,null));
        Point center5 = new Point(12.9385403535156, 12.912063808435725);
        Distance radius5 = new Distance(1200, Metrics.KILOMETERS);
        Circle within5 = new Circle(center5, radius5);
        System.out.println("第五个:"+geoService.getPointRadius(within5,null));
        Point center6 = new Point(13.9385403535156, 1.912063808435725);
        Distance radius6 = new Distance(1200, Metrics.KILOMETERS);
        Circle within6 = new Circle(center6, radius6);
        System.out.println("第六个:"+geoService.getPointRadius(within6,null));
        Point center7 = new Point(77.9385403535156, 12.912063808435725);
        Distance radius7 = new Distance(1200, Metrics.KILOMETERS);
        Circle within7 = new Circle(center7, radius7);
        System.out.println("第七个:"+geoService.getPointRadius(within7,null));
        Point center8 = new Point(88.9385403535156, 12.912063808435725);
        Distance radius8 = new Distance(1200, Metrics.KILOMETERS);
        Circle within8 = new Circle(center8, radius8);
        System.out.println("第八个:"+geoService.getPointRadius(within8,null));

    }

//    @PostConstruct
//    public void inserTest() throws InterruptedException {
//        for (int i=0;i<10;i++){
//            redisService.setRedis(i+"",i+"");
//            sleep(1000);
//        }
//    }

}
