package com.gyan.pg_management.modules.inventory.api;

import com.gyan.pg_management.modules.inventory.dto.request.PropertyCreateRequest;
import com.gyan.pg_management.modules.inventory.dto.response.PropertyResponse;
import com.gyan.pg_management.modules.inventory.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    @PostMapping("/create")
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyCreateRequest request){
        PropertyResponse response = propertyService.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<PropertyResponse[]> getAllProperties(@RequestParam(name = "ownerId") Long ownerId){
        PropertyResponse[] response = propertyService.getAllProperties(ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
