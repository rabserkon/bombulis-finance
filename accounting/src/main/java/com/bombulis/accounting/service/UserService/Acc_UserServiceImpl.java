package com.bombulis.accounting.service.UserService;

import com.bombulis.accounting.entity.Acc_Role;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.repository.Acc_RoleRepository;
import com.bombulis.accounting.repository.Acc_UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class Acc_UserServiceImpl implements Acc_UserService {

    private Acc_UserRepository userRepository;


    private Acc_RoleRepository roleRepository;

    @Override
    public Acc_User createUser(Long id) {
        Acc_User newUser = new Acc_User();
        newUser.setUserId(id);
        newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newUser = userRepository.save(newUser);
        newUser.setRoles(this.addBaseRoles(newUser));
        return newUser;
    }

    @Override
    public Acc_User findUserById(Long id) throws Acc_UserException {
        Acc_User user = userRepository.findUserByUserId(id)
                .orElseThrow(()->new Acc_UserException("User not found"));;
        return user;
    }

    @Override
    public Acc_User existUser(Long userId) {
        Acc_User user = userRepository.findUserByUserId(userId).orElseGet(() -> null);
        if (user == null){
            return this.createUser(userId);
        }
        return user;
    }


    @Override
    public List<Acc_Role> addBaseRoles(Acc_User user) {
        List<Acc_Role> roleList = new ArrayList<>();
        roleList.add(new Acc_Role("ROLE_USER",user));
        roleList.add(new Acc_Role("ROLE_ACCOUNTS",user));
        roleList.add(new Acc_Role("ROLE_RATE",user));
        roleList.add(new Acc_Role("ROLE_TRANSACTIONS", user));
        return  roleRepository.saveAll(roleList);
    }


    @Autowired
    public void setRoleRepository(Acc_RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(Acc_UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
