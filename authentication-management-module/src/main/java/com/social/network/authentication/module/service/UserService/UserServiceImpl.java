package com.social.network.authentication.module.service.UserService;

import com.social.network.authentication.module.dto.UserDTO;
import com.social.network.authentication.module.entity.Role;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.repository.RoleRepository;
import com.social.network.authentication.module.repository.UserRepository;
import com.social.network.authentication.module.service.RegistrationService.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public boolean checkExistsThisUser(String email, String login) throws RegistrationException {
        User newUser = userRepository.findUserByEmailOrLogin(email, login);
        return newUser != null;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User createAccount(UserDTO user) throws RegistrationException {
        if (checkExistsThisUser(user.getEmail(), user.getLogin())){
            throw new RegistrationException("This user exist");
        }
        User createUser = new User();
        createUser.setAccountNonLocked(true);
        createUser.setUuid(UUID.randomUUID().toString());
        createUser.setEmail(user.getEmail());
        createUser.setLogin(user.getLogin());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setDateUpdateEmail(LocalDateTime.now());
        createUser.setFirstEmail(user.getEmail());
        createUser.setLastEmail(user.getEmail());
        createUser.setEnabled(false);
        return userRepository.save(createUser);
    }

    @Override
    @Transactional
    public void addRoles(User user, String... roles) {
        List<Role> rolesList = new ArrayList<>();
        for (String role : roles) {
            Role roleCreate = new Role();
            roleCreate.setUser(user);
            roleCreate.setRoleName(String.format("ROLE_%s", role.toUpperCase()));
            rolesList.add(roleCreate);
        }
        roleRepository.saveAll(rolesList);
    }



    @Override
    @Transactional
    public User setEnabledUser(User user, boolean status) {
        user.setEnabled(status);
        return userRepository.save(user);
    }
}
