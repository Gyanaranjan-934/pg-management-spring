package com.gyan.pg_management.modules.inventory.dto.response;

import com.gyan.pg_management.modules.inventory.domain.BedStatus;
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
