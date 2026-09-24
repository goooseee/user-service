package com.example.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DAO.UserDAO;
import com.example.DTO.UserDTOResponse;
import com.example.entity.User;
import com.example.service.UserService;

@ExtendWith(MockitoExtension.class)
public class FindByIdTest {
	
	@Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;
	
    @Test
    @DisplayName("Должен вернуть Optional с пользователем, если он найден")
    void findById_UserExists_ReturnsUser() {
        Long userId = 1L;
        User mockUser = new User("Анна", "anna@example.com", 30);
        mockUser.setId(userId);

        when(userDao.findById(userId)).thenReturn(Optional.of(mockUser));

        Optional<UserDTOResponse> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals("Анна", result.get().name());
        assertEquals("anna@example.com", result.get().email());
        verify(userDao).findById(userId);
    }

    @Test
    @DisplayName("Должен вернуть Optional.empty(), если пользователь не найден")
    void findById_UserNotFound_ReturnsEmpty() {
        Long userId = 99L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDTOResponse> result = userService.findById(userId);

        assertFalse(result.isPresent());
        verify(userDao).findById(userId);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -5L})
    @DisplayName("Должен вернуть Optional.empty() без обращения к DAO при невалидном ID")
    void findById_InvalidId_ReturnsEmptyWithoutCallingDao(Long invalidId) {
        Optional<UserDTOResponse> result = userService.findById(invalidId);

        assertFalse(result.isPresent());
        verify(userDao, never()).findById(any());
    }
}
