package com.example.demo.controller.neo4j.entity;

import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author：guoyq
 * @name：UserEntity
 * @Date：2025/4/9 14:58
 * @Filename：UserEntity
 */
@Node("User")
public class Person implements Serializable {

    //neo4j自动生成的id
    @Id
    @GeneratedValue
    private Long id;

    //节点的属性
    @Property
    private String name;
    private int age;
    @Relationship(type = "FRIEND", direction = Relationship.Direction.OUTGOING)
    private List<Person> friends = new ArrayList<>();

    public Person() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public void setFriends(List<Person> friends) {
        this.friends = friends;
    }
}
