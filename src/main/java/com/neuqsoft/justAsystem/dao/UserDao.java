package com.neuqsoft.justAsystem.dao;

import com.neuqsoft.justAsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long>{
    User findUserByUsername(String username);

}
