package com.bombulis.accounting.service.UserService;

import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Override
    public User createUser(Long id) {
        User newUser = new User(id);
        newUser = userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User findUser(Long id) throws NotFoundUser {
        User user = userRepository.findUserByUserId(id);
        if (user == null){
            throw new NotFoundUser("User not found");
        }
        return user;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
