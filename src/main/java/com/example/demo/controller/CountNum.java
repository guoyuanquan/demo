package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CountNum {
    @PostConstruct
    public void count(){
        int i=100;
        int num=0;
        for (int j=0;j<i;j++){
            num=num+i;
            log.info(i+"-和："+num);
        }
    }
}
