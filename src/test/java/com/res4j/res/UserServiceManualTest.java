package com.res4j.res;

import com.res4j.res.Model.User;
import com.res4j.res.Repo.UserRepository;
import com.res4j.res.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceManualTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Simulate database failure
        when(userRepository.findAll()).thenThrow(new RuntimeException("Simulated database failure"));
    }

    @Test
    public void testFallbackOnDatabaseFailure() throws SQLException {
        // Make a request to the service and check the fallback behavior
        List<User> result = userService.findAllUsers();
        assertEquals(1, result.size());
        assertEquals("Can't load info, try again later", result.get(0).getName());
        System.out.println("Ran");
    }
}
