package com.example.demo.controller.neo4j.service;

import com.example.demo.controller.neo4j.entity.RelatlionShip;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author：guoyq
 * @name：PersonRelatlionShipRepository
 * @Date：2025/4/9 15:33
 * @Filename：PersonRelatlionShipRepository
 */
@Repository
public interface PersonRelatlionShipRepository extends Neo4jRepository<RelatlionShip,Long> {
    @Query("match (n:Person {name: $from }), (m:Person {name: $to})  create (n)-[:FRIEND{relation: $relation}]->(m)")
    void createRelation(@Param(value = "from") String from, @Param(value = "to") String to, @Param(value = "relation") String relation);
}
