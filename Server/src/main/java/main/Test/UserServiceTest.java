package main.Test;

import main.DAO.UserDAO;
import main.Models.Entities.User;
import main.Services.UserService;
import org.hibernate.HibernateError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    private UserDAO userDAOMock;

    @BeforeEach
    void setUp() {
        userDAOMock = mock(UserDAO.class);
        userService = new UserService();
        userService.userDAO = userDAOMock;
    }

    @Test
    void testFindEntity() {
        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        when(userDAOMock.findById(userId)).thenReturn(mockUser);

        User user = (User) userService.findEntity(userId);

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    @Test
    void testSaveEntity() {
        User user = new User();
        user.setUserId(1);

        userService.saveEntity(user);

        verify(userDAOMock, times(1)).save(user);
    }

    @Test
    void testDeleteEntity() {
        User user = new User();
        user.setUserId(1);

        userService.deleteEntity(user);

        verify(userDAOMock, times(1)).delete(user);
    }

    @Test
    void testUpdateEntity() {
        User user = new User();
        user.setUserId(1);

        userService.updateEntity(user);

        verify(userDAOMock, times(1)).update(user);
    }

    @Test
    void testFindAllEntities() {
        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = Arrays.asList(user1, user2);
        when(userDAOMock.findAll()).thenReturn(mockUsers);

        List<User> users = userService.findAllEntities();

        assertNotNull(users);
        assertEquals(2, users.size());
    }
}