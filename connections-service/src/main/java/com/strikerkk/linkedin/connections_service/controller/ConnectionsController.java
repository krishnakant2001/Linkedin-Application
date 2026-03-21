package com.strikerkk.linkedin.connections_service.controller;

import com.strikerkk.linkedin.connections_service.entity.Person;
import com.strikerkk.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(@RequestHeader("X-user-Id") Long userId) {
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections(userId));
    }
}
