package com.example.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DAO.UserDAO;
import com.example.DTO.UserDTORequest;
import com.example.entity.User;
import com.example.service.UserService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {
	
	@Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Должен успешно обновить данные существующего пользователя")
    void updateUser_Success() {
        Long userId = 1L;
        User existingUser = new User("Старое Имя", "old@mail.com", 20);
        existingUser.setId(userId);

        UserDTORequest updateDTO = new UserDTORequest("Новое Имя", "new@mail.com", 21);

        when(userDao.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.updateUser(userId, updateDTO);

        assertEquals("Новое Имя", existingUser.getName());
        assertEquals("new@mail.com", existingUser.getEmail());
        assertEquals(21, existingUser.getAge());

        verify(userDao).update(existingUser);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если пользователь для обновления не найден")
    void updateUser_UserNotFound_ThrowsException() {
        Long userId = 99L;
        UserDTORequest updateDTO = new UserDTORequest("Новое Имя", "new@mail.com", 21);

        when(userDao.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(userId, updateDTO)
        );

        assertEquals("Пользователь с ID " + userId + " не найден", ex.getMessage());
        verify(userDao, never()).update(any());
    }
	
}
