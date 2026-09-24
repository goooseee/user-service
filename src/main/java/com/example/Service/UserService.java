package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.DAO.UserDAO;
import com.example.DAO.UserDAOImpl;
import com.example.DTO.UserDTORequest;
import com.example.DTO.UserDTOResponse;
import com.example.entity.User;
import com.example.mapper.UserMapper;

public class UserService {

    private final UserDAO userDao;
    
    private final UserMapper userMapper = new UserMapper();

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public UserDTOResponse createUser(String name, String email, int age) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email");
        }
        User user = new User(name, email, age);
        userDao.save(user);
        return userMapper.userToDTO(user);
    }

    public Optional<UserDTOResponse> findById(Long id) {
        return userDao.findById(id).map( userMapper::userToDTO );
    }

    public List<UserDTOResponse> findAll() {
        return userDao.findAll()
        		.stream()
        		.map( userMapper::userToDTO )
        		.toList();
    }

    public void updateUser(Long id, UserDTORequest userDTO) {
    	User user = userDao.findById( id )
    			.orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));;
    	user.setName(userDTO.name());
    	user.setEmail( userDTO.email() );
    	user.setAge(userDTO.age());
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}
