package com.bombulis.accounting.service.UserService;

import com.bombulis.accounting.entity.User;

public interface UserService {
    User createUser (Long id);
    User findUser (Long id) throws NotFoundUser;
}
