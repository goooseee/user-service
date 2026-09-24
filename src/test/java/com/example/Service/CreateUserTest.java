package com.example.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DAO.UserDAO;
import com.example.DTO.UserDTORequest;
import com.example.DTO.UserDTOResponse;
import com.example.entity.User;
import com.example.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {
	
	@Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Должен успешно создать пользователя при валидных данных")
    void createUser_Success() {
        String name = "Иван";
        String email = "ivan@example.com";
        int age = 25;

        UserDTOResponse result = userService.createUser(new UserDTORequest(name, email, age));

        assertNotNull(result);
        assertEquals(name, result.name());
        assertEquals(email, result.email());
        assertEquals(age, result.age());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(name, savedUser.getName());
        assertEquals(email, savedUser.getEmail());
        assertEquals(age, savedUser.getAge());
    }

    @Test
    @DisplayName("Должен выбросить исключение, если имя пустое")
    void createUser_BlankName_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(new UserDTORequest("   ", "ivan@example.com", 25))
        );

        assertEquals("Имя не может быть пустым", ex.getMessage());
        verify(userDao, never()).save(any());
    }

    @Test
    @DisplayName("Должен выбросить исключение при некорректном email")
    void createUser_InvalidEmail_ThrowsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(new UserDTORequest("Иван", "invalid-email.com", 25))
        );

        assertEquals("Некорректный формат email", ex.getMessage());
        verify(userDao, never()).save(any());
    }
	
    @ParameterizedTest
    @ValueSource(ints = {-1, 151})
    @DisplayName("Должен выбросить исключение, если возраст выходит за пределы [0..150]")
    void createUser_InvalidAge_ThrowsException(int invalidAge) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(new UserDTORequest("Иван", "ivan@example.com", invalidAge))
        );

        assertEquals("Возраст должен быть в диапазоне от 0 до 150", ex.getMessage());
        verify(userDao, never()).save(any());
    }
}
