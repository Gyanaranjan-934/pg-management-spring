package com.gyan.pg_management.dto.response.bed;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BedResponse {
    Long bedId;
    String bedNumber;
    String roomNumber;
}
