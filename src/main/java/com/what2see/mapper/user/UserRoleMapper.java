package com.what2see.mapper.user;

import com.what2see.dto.user.UserRole;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;

public class UserRoleMapper {
    public static UserRole mapUserToRole(User user) {
        if (user instanceof Tourist) {
            return UserRole.TOURIST;
        } else if (user instanceof Guide) {
            return UserRole.GUIDE;
        } else if (user instanceof Administrator) {
            return UserRole.ADMINISTRATOR;
        }
        throw new RuntimeException("Role not found!");
    }
}
