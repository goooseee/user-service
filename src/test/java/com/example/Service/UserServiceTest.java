package com.example.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DAO.UserDAO;
import com.example.DTO.UserDTOResponse;
import com.example.entity.User;
import com.example.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;
	
    @Test
    void createUser_ShouldSaveUser_WhenEmailIsValid() {
        String name = "Иван";
        String email = "ivan@example.com";
        int age = 25;

        UserDTOResponse createdUser = userService.createUser(name, email, age);

        assertNotNull(createdUser);
        assertEquals(name, createdUser.name());
        assertEquals(email, createdUser.email());
        verify(userDao, times(1)).save(any(User.class));
    }
    
    @Test
    void createUser_ShouldThrowException_WhenEmailIsInvalid() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> userService.createUser("Иван", "invalid-email", 25)
        );

        assertEquals("Некорректный email", exception.getMessage());
        verify(userDao, never()).save(any());
    }
    
    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        User expectedUser = new User("Петр", "petr@example.com", 30);
        when(userDao.findById(1L)).thenReturn(Optional.of(expectedUser));

        Optional<UserDTOResponse> actualUser = userService.findById(1L);

        assertTrue(actualUser.isPresent());
        assertEquals("Петр", actualUser.get().name());
        verify(userDao, times(1)).findById(1L);
    }
}
