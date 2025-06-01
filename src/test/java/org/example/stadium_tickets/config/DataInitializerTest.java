package org.example.stadium_tickets.config;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.repository.RoleRepository;
import org.example.stadium_tickets.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRun_WhenRolesAndAdminExist_ShouldSkipInitialization() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(2L);
        when(userService.existsByUsername("admin")).thenReturn(true);

        // Act
        dataInitializer.run();

        // Assert
        verify(roleRepository, never()).save(any(Role.class));
        verify(userService, never()).createUser(any(User.class));
        verify(userService, never()).addAdminRole(anyLong());
    }

    @Test
    void testRun_WhenRolesAndAdminDoNotExist_ShouldInitialize() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(0L);
        when(userService.existsByUsername("admin")).thenReturn(false);
        when(roleRepository.existsByName("USER")).thenReturn(false);
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);

        User adminUser = new User("admin", "admin", "admin@example.com");
        adminUser.setId(1L);
        when(userService.createUser(any(User.class))).thenReturn(adminUser);

        // Act
        dataInitializer.run();

        // Assert
        verify(roleRepository, times(1)).save(argThat(role -> role.getName().equals("USER")));
        verify(roleRepository, times(1)).save(argThat(role -> role.getName().equals("ADMIN")));
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(1L);
    }

    @Test
    void testRun_WhenUserRoleDoesNotExist_ShouldCreateUserRole() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(0L); // No roles exist
        when(userService.existsByUsername("admin")).thenReturn(false); // Admin doesn't exist
        when(roleRepository.existsByName("USER")).thenReturn(false); // USER role doesn't exist
        when(roleRepository.existsByName("ADMIN")).thenReturn(true); // ADMIN role exists

        User adminUser = new User("admin", "admin", "admin@example.com");
        adminUser.setId(1L);
        when(userService.createUser(any(User.class))).thenReturn(adminUser);

        // Act
        dataInitializer.run();

        // Assert
        verify(roleRepository, times(1)).save(argThat(role -> role.getName().equals("USER")));
        verify(roleRepository, never()).save(argThat(role -> role.getName().equals("ADMIN")));
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(anyLong());
    }

    @Test
    void testRun_WhenAdminRoleDoesNotExist_ShouldCreateAdminRole() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(0L); // No roles exist
        when(userService.existsByUsername("admin")).thenReturn(false); // Admin doesn't exist
        when(roleRepository.existsByName("USER")).thenReturn(true); // USER role exists
        when(roleRepository.existsByName("ADMIN")).thenReturn(false); // ADMIN role doesn't exist

        User adminUser = new User("admin", "admin", "admin@example.com");
        adminUser.setId(1L);
        when(userService.createUser(any(User.class))).thenReturn(adminUser);

        // Act
        dataInitializer.run();

        // Assert
        verify(roleRepository, never()).save(argThat(role -> role.getName().equals("USER")));
        verify(roleRepository, times(1)).save(argThat(role -> role.getName().equals("ADMIN")));
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(anyLong());
    }

    @Test
    void testRun_WhenAdminUserDoesNotExist_ShouldCreateAdminUser() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(0L); // No roles exist
        when(userService.existsByUsername("admin")).thenReturn(false); // Admin doesn't exist
        when(roleRepository.existsByName("USER")).thenReturn(true); // USER role exists
        when(roleRepository.existsByName("ADMIN")).thenReturn(true); // ADMIN role exists

        User adminUser = new User("admin", "admin", "admin@example.com");
        adminUser.setId(1L);
        when(userService.createUser(any(User.class))).thenReturn(adminUser);

        // Act
        dataInitializer.run();

        // Assert
        verify(roleRepository, never()).save(any(Role.class));
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(1L);
    }

    @Test
    void testRun_WhenExceptionOccursDuringRoleInitialization_ShouldContinue() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(0L);
        when(userService.existsByUsername("admin")).thenReturn(false);
        when(roleRepository.existsByName("USER")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenThrow(new RuntimeException("Test exception"));

        User adminUser = new User("admin", "admin", "admin@example.com");
        adminUser.setId(1L);
        when(userService.createUser(any(User.class))).thenReturn(adminUser);

        // Act
        dataInitializer.run();

        // Assert
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(1L);
    }

    @Test
    void testRun_WhenExceptionOccursDuringUserCreation_ShouldContinue() throws Exception {
        // Arrange
        when(roleRepository.count()).thenReturn(0L);
        when(userService.existsByUsername("admin")).thenReturn(false);
        when(roleRepository.existsByName("USER")).thenReturn(false);
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Test exception"));

        // Act
        dataInitializer.run();

        // Assert
        verify(roleRepository, times(1)).save(argThat(role -> role.getName().equals("USER")));
        verify(roleRepository, times(1)).save(argThat(role -> role.getName().equals("ADMIN")));
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, never()).addAdminRole(anyLong());
    }
}
