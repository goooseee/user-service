package com.example.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
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
public class FindAllTest {
	
	@Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;
	
    @Test
    @DisplayName("findAll должен конвертировать список сущностей в список DTO")
    void findAll_ReturnsListOfDTOs() {
        User user1 = new User("Алексей", "alex@mail.com", 20);
        User user2 = new User("Мария", "maria@mail.com", 22);
        when(userDao.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTOResponse> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals("Алексей", result.get(0).name());
        assertEquals("Мария", result.get(1).name());
        verify(userDao).findAll();
    }
    
}
