package com.example.DAO;

import java.util.List;
import java.util.Optional;

import com.example.Entity.User;

public interface UserDAO {
	void save(User user);
	Optional<User> findById(Long id);
	List<User> findAll();
	void update(User user);
	void delete(Long id);
}
