package com.github.zubmike.service.demo.dao;

import com.github.zubmike.service.demo.types.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByLogin(String login);

}
