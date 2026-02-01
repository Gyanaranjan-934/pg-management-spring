package com.gyan.pg_management.dto.response.bed;

import com.gyan.pg_management.enums.BedStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BedResponse {
    Long bedId;
    String bedNumber;
    String roomNumber;
    BedStatus bedStatus;
}
