package com.example.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DAO.UserDAO;
import com.example.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class DeleteUserTest {
	
	@Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Должен вызвать метод удаления у DAO при валидном ID")
    void deleteUser_Success() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userDao).delete(userId);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @DisplayName("Должен выбросить исключение при удалении с некорректным ID")
    void deleteUser_InvalidId_ThrowsException(Long invalidId) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(invalidId)
        );

        assertEquals("Некорректный ID пользователя", ex.getMessage());
        verify(userDao, never()).delete(any());
    }
}
