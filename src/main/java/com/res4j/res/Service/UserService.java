package com.res4j.res.Service;

import com.res4j.res.Model.User;
import com.res4j.res.Repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    int retryCount =1;
    //@CircuitBreaker(name = "userService", fallbackMethod = "fallbackFindAllUsers")
    @Retry(name = "userService",fallbackMethod = "fallbackFindAllUsers")
    public List<User> findAllUsers() throws SQLException {
        logger.info("Attempting to find all users "+retryCount++);
        throw new SQLException("Simulated database error");
        // return userRepository.findAll();
    }

// http://localhost:9090/actuator/retryevents
    public List<User> fallbackFindAllUsers(Throwable throwable) {
        User fallbackUser = new User();
        logger.error("Fallback method executed due to: ", throwable);
        fallbackUser.setName("Can't load info, try again later"); // Assuming User has a setName method
        List<User> fallbackList = new ArrayList<>();
        fallbackList.add(fallbackUser);
//        ex.printStackTrace();
        return fallbackList;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
