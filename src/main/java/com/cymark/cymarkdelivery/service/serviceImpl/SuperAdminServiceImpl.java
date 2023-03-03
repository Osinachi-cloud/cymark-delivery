package com.cymark.cymarkdelivery.service.serviceImpl;

import com.cymark.cymarkdelivery.dtos.response.SwitchRoleResponse;
import com.cymark.cymarkdelivery.exceptions.UserNotFoundException;
import com.cymark.cymarkdelivery.model.Role;
import com.cymark.cymarkdelivery.model.RoleRequest;
import com.cymark.cymarkdelivery.model.User;
import com.cymark.cymarkdelivery.repository.UserRepository;
import com.cymark.cymarkdelivery.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService{

    private final UserRepository userRepository;



    @Override
    public SwitchRoleResponse switchRole(RoleRequest roleRequest) {
        Optional<User> user =  userRepository.findByEmail(roleRequest.getEmail());
        if(!user.isPresent()){
            throw new UserNotFoundException("User not found" + "with " + roleRequest.getEmail());
        }
        boolean isRolePresent = findRole(user.get().getRoles(), roleRequest);
        if(isRolePresent){
            return SwitchRoleResponse.builder()
                    .message("User already exists with role" + roleRequest.getRoleName())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        var newRole = Role.builder()
                        .presentRole("ADMIN")
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();

        user.get().getRoles().add(newRole);
        userRepository.save(user.get());
        return SwitchRoleResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("User has been successfully switched to" + roleRequest.getRoleName() + "Role")
                .build();
    }

    @Override
    public void deleteUserByEmail(String email) {
       userRepository.deleteByEmail(email);
    }

    public boolean findRole(List<Role> userRoles, RoleRequest roleRequest){
        boolean isRolePresent = false;
        for(Role role: userRoles){
            if(role.getPresentRole() == roleRequest.getRoleName()){
                isRolePresent = true;
            }else{
                isRolePresent = false;
            }
        }
        return isRolePresent;

    }
}
