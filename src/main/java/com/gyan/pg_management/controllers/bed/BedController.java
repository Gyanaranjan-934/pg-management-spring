package com.gyan.pg_management.controllers.bed;

import com.gyan.pg_management.dto.request.bed.BedCreateRequest;
import com.gyan.pg_management.dto.request.bed.BedUpdateRequest;
import com.gyan.pg_management.dto.response.bed.BedResponse;
import com.gyan.pg_management.service.bed.BedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/bed")
public class BedController {
    private final BedService bedService;

    @PostMapping("/create")
    public ResponseEntity<BedResponse> createBed(@Valid @RequestBody BedCreateRequest request){
        BedResponse response = bedService.createBed(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BedResponse> updateBed(@Valid @RequestBody BedUpdateRequest request){
        BedResponse response = bedService.updateBed(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
