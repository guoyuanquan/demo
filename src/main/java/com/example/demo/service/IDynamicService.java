package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.model.DynamicReceive;
import org.springframework.stereotype.Service;


public interface IDynamicService extends IService<DynamicReceive> {
    void save(String msg);
}
