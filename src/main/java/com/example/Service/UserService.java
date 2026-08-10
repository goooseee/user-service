package com.example.Service;

import java.util.List;
import java.util.Optional;

import com.example.DAO.UserDAO;
import com.example.DAO.UserDAOImpl;
import com.example.Entity.User;

public class UserService {

    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email, int age) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email");
        }
        User user = new User(name, email, age);
        userDao.save(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}
