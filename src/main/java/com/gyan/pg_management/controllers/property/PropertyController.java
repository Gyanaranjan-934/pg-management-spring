package com.gyan.pg_management.controllers.property;

import com.gyan.pg_management.dto.request.property.PropertyCreateRequest;
import com.gyan.pg_management.dto.response.property.PropertyResponse;
import com.gyan.pg_management.service.property.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    public ResponseEntity<PropertyResponse> createProperty(@RequestBody PropertyCreateRequest request){
        PropertyResponse response = propertyService.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
