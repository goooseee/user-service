package com.example.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.entity.User;

@Testcontainers
public class UserDAOImplIntegrationTest {
	
	@Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
	
	private static UserDAO userDao;
	
	@BeforeAll
    static void startContainer() {
        postgres.start();
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        userDao = new UserDAOImpl();
    }
	
	@Test
    void saveAndFindById_ShouldPersistUserInDatabase() {
        User user = new User("Алексей", "alex@example.com", 28);

        userDao.save(user);
        Optional<User> foundUser = userDao.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Алексей", foundUser.get().getName());
    }
	
	@Test
    void delete_ShouldRemoveUserFromDatabase() {
        User user = new User("Ольга", "olga@example.com", 22);
        userDao.save(user);
        Long userId = user.getId();

        userDao.delete(userId);
        Optional<User> foundUser = userDao.findById(userId);

        assertFalse(foundUser.isPresent());
    }
	
}
