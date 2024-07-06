package com.bombulis.accounting.service.UserService;

import com.bombulis.accounting.entity.Role;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.repository.RoleRepository;
import com.bombulis.accounting.repository.UserRepository;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;


    private RoleRepository roleRepository;

    @Override
    public User createUser(Long id) {
        User newUser = new User();
        newUser.setUserId(id);
        newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newUser = userRepository.save(newUser);
        newUser.setRoles(this.addBaseRoles(newUser));
        return newUser;
    }

    @Override
    public User findUserById(Long id) throws UserException {
        User user = userRepository.findUserByUserId(id)
                .orElseThrow(()->new UserException("User not found"));;
        return user;
    }

    @Override
    public User existUser(Long userId) {
        User user = userRepository.findUserByUserId(userId).orElseGet(() -> null);
        if (user == null){
            return this.createUser(userId);
        }
        return user;
    }


    @Override
    public List<Role> addBaseRoles(User user) {
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("ROLE_USER",user));
        return  roleRepository.saveAll(roleList);
    }


    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
