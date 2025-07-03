package com.example.demo.controller.neo4j.service;

import com.example.demo.controller.neo4j.entity.Person;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author：guoyq
 * @name：PersonRepository
 * @Date：2025/4/9 15:27
 * @Filename：PersonRepository
 */
@Repository
public interface PersonRepository extends Neo4jRepository<Person,Long> {
    @Query("Match (p:Person) return p")
    List<Person> findParentList();

    Person findByName(String name);

    @Query("MATCH (u:Person)-[:FRIEND]->(f:Person) WHERE u.name = $name RETURN u,f")
    List<Person> findFriendsByName(@Param(value = "name") String name);


}
