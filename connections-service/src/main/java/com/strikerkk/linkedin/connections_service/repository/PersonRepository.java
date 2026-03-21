package com.strikerkk.linkedin.connections_service.repository;

import com.strikerkk.linkedin.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (p1:Person) -[:CONNECTED_TO]- (p2:Person) " +
            "WHERE p1.userId = $userId " +
            "RETURN p2")
    List<Person> getFirstDegreeConnections(Long userId);
}
