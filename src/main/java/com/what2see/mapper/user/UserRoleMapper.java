package com.what2see.mapper.user;

import com.what2see.dto.user.UserRole;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;

/**
 * Utility class that maps {@link User} entities to {@link UserRole} enums based on their subclass.<br>
 * Is usually used in controller to communicate with client side and let it be more flexible.
 */
public class UserRoleMapper {

    /**
     * Maps a {@link User} entity to a {@link UserRole} enum based on its concrete subclass
     * @param user entity to be mapped
     * @return mapped enum
     * @see UserRole
     */
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
