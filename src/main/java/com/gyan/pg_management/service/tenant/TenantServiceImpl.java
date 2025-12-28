package com.gyan.pg_management.service.tenant;

import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService{

    private final TenantRepository tenantRepository;

    public Tenant createTenant(Tenant tenant) {
        tenant.setActive(true);
        return tenantRepository.save(tenant);
    }

    public void deactivateTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        tenant.setActive(false);
        tenantRepository.save(tenant);
    }
}
