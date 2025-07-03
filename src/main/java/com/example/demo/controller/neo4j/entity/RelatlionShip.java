package com.example.demo.controller.neo4j.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

import java.io.Serializable;

/**
 * @Author：guoyq
 * @name：RelatlionShip
 * @Date：2025/4/9 15:28
 * @Filename：RelatlionShip
 */
@RelationshipEntity(type="personRelationShip")
public class RelatlionShip implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    //起始节点
    @StartNode
    private Person startPerson;

    //结束节点
    @EndNode
    private Person endPerson;

    //属性
    @Property
    private String relation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getStartPerson() {
        return startPerson;
    }

    public void setStartPerson(Person startPerson) {
        this.startPerson = startPerson;
    }

    public Person getEndPerson() {
        return endPerson;
    }

    public void setEndPerson(Person endPerson) {
        this.endPerson = endPerson;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
