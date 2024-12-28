package com.elisbao.restapispring;

import com.elisbao.restapispring.models.Task;
import com.elisbao.restapispring.models.User;
import com.elisbao.restapispring.models.enums.ProfileEnum;
import com.elisbao.restapispring.repositories.TaskRepository;
import com.elisbao.restapispring.security.UserSpringSecurity;
import com.elisbao.restapispring.services.TaskService;
import com.elisbao.restapispring.services.UserService;
import com.elisbao.restapispring.services.exceptions.AuthorizationException;
import com.elisbao.restapispring.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeAll
    static void setUp() {
        UserSpringSecurity userSpringSecurity = new UserSpringSecurity(1L, "test", "password", new HashSet<>(List.of(ProfileEnum.USER)));
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userSpringSecurity);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    public void setUpEach() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testFindByIdSuccess() {

        // Mock task and its user
        Task mockTask = new Task();
        mockTask.setId(1L);
        User mockUser = new User();
        mockUser.setId(1L); // Match with authenticated user
        mockTask.setUser(mockUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        // Call and validate
        Task foundTask = taskService.findById(1L);
        assertNotNull(foundTask);
        assertEquals(1L, foundTask.getId());
    }

    @Test
    public void testFindByIdTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> taskService.findById(1L));
        assertEquals("Tarefa não encontrada! Id: 1, Tipo: com.elisbao.restapispring.models.Task", ex.getMessage());
    }

    @Test
    public void testFindByIdUnauthorized() {

        // Mock task and its user
        Task mockTask = new Task();
        mockTask.setId(1L);
        User mockUser = new User();
        mockUser.setId(2L); // Task owned by this user
        mockTask.setUser(mockUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        // Call and validate exception
        AuthorizationException ex = assertThrows(AuthorizationException.class, () -> taskService.findById(1L));
        assertEquals("Acesso negado!", ex.getMessage());
    }
}
