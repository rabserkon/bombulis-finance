package com.bombulis.accounting.service.UserService;

import com.bombulis.accounting.entity.Acc_Role;
import com.bombulis.accounting.entity.Acc_User;

import java.util.List;

public interface Acc_UserService {
    Acc_User createUser (Long id);
    Acc_User findUserById(Long id) throws Acc_UserException;

    Acc_User existUser(Long userId);

    List<Acc_Role> addBaseRoles(Acc_User user);
}
