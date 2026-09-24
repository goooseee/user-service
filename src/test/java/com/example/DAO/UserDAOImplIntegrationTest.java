package com.example.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	
	@BeforeEach
    void cleanDatabase() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            userDao.delete(user.getId());
        }
    }
	
	@Test
    @DisplayName("Сохранение и поиск по ID")
    void saveAndFindById_ShouldPersistUserInDatabase() {
        User user = new User("Алексей", "alex@example.com", 28);

        userDao.save(user);
        
        assertNotNull(user.getId(), "После сохранения пользователь должен получить ID");

        Optional<User> foundUser = userDao.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Алексей", foundUser.get().getName());
        assertEquals("alex@example.com", foundUser.get().getEmail());
        assertEquals(28, foundUser.get().getAge());
    }

    @Test
    @DisplayName("Получение всех пользователей (findAll)")
    void findAll_ShouldReturnAllUsers() {
        User user1 = new User("Иван", "ivan@example.com", 20);
        User user2 = new User("Мария", "maria@example.com", 25);

        userDao.save(user1);
        userDao.save(user2);

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Обновление данных пользователя (update)")
    void update_ShouldUpdateExistingUser() {
        User user = new User("Сергей", "sergey@example.com", 30);
        userDao.save(user);
        Long userId = user.getId();

        user.setName("Сергей Обновленный");
        user.setEmail("sergey_new@example.com");
        user.setAge(31);

        userDao.update(user);

        Optional<User> updatedUserOpt = userDao.findById(userId);

        assertTrue(updatedUserOpt.isPresent());
        User updatedUser = updatedUserOpt.get();
        assertEquals("Сергей Обновленный", updatedUser.getName());
        assertEquals("sergey_new@example.com", updatedUser.getEmail());
        assertEquals(31, updatedUser.getAge());
    }

    @Test
    @DisplayName("Удаление пользователя (delete)")
    void delete_ShouldRemoveUserFromDatabase() {
        User user = new User("Ольга", "olga@example.com", 22);
        userDao.save(user);
        Long userId = user.getId();

        userDao.delete(userId);
        Optional<User> foundUser = userDao.findById(userId);

        assertFalse(foundUser.isPresent());
    }	
}
