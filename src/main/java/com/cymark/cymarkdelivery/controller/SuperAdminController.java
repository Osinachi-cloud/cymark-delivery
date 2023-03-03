package com.cymark.cymarkdelivery.controller;

import com.cymark.cymarkdelivery.dtos.response.SwitchRoleResponse;
import com.cymark.cymarkdelivery.model.RoleRequest;
import com.cymark.cymarkdelivery.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/demo-controller")
public class SuperAdminController {

    private final SuperAdminService superAdminService;


    @GetMapping("/hello")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/hi")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("Hi from secured endpoint");
    }

    @PostMapping("/switchRole")
    public ResponseEntity<SwitchRoleResponse> switchUserRole(@RequestBody RoleRequest roleSwitch){
        return ResponseEntity.ok(superAdminService.switchRole(roleSwitch));
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteByEmail(@PathVariable String email){
        superAdminService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
