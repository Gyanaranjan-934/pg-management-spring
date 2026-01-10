package com.gyan.pg_management.service.tenant;

import com.gyan.pg_management.entity.Tenant;

public interface TenantService {
    Tenant createTenant(Tenant tenant);
    void deactivateTenant(Long tenantId);
    Tenant getTenant(Long tenantId);
}
