package com.cymark.cymarkdelivery.service;

import com.cymark.cymarkdelivery.dtos.response.SwitchRoleResponse;
import com.cymark.cymarkdelivery.model.RoleRequest;

public interface SuperAdminService {
    SwitchRoleResponse switchRole(RoleRequest roleSwitch);
    void deleteUserByEmail(String email);
}
