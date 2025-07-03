package com.example.demo;

import com.example.demo.controller.neo4j.entity.Person;
import com.example.demo.controller.neo4j.service.PersonRelatlionShipRepository;
import com.example.demo.controller.neo4j.service.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author：guoyq
 * @name：DemoApplicationTests
 * @Date：2025/4/9 15:34
 * @Filename：DemoApplicationTests
 */
@Slf4j
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonRelatlionShipRepository personRelatlionShipRepository;

    @Test
    void testCreateEntity() {
        //增加节点
        Person newPerson = new Person();
        newPerson.setName("杀人犯2");
        newPerson.setAge(20);
        personRepository.save(newPerson);
    }
    @Test
    void testCreateRelation(){
        personRelatlionShipRepository.createRelation("杀人犯","盗贼","朋友");
    }

    @Test
    void tesstFind(){
        List<Person> people= personRepository.findFriendsByName("杀人犯");
        log.info(""+people);
    }
}
