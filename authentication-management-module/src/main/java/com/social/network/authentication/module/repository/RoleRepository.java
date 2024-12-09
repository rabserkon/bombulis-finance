package com.social.network.authentication.module.repository;

import com.social.network.authentication.module.entity.Role;
import com.social.network.authentication.module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUser(User user);

}
