package com.bombulis.accounting.service.UserService;

import com.bombulis.accounting.entity.Role;
import com.bombulis.accounting.entity.User;

import java.util.List;

public interface UserService {
    User createUser (Long id);
    User findUserById(Long id) throws UserException;

    User existUser(Long userId);

    List<Role> addBaseRoles(User user);
}
