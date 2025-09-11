package com.developmentprep.journalApp.service;

import com.developmentprep.journalApp.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTests {

    @Autowired
    UserService userService;

    @Disabled
    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testSaveNewUser(User user) {
        assertTrue(userService.saveNewUser(user));
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 10, 12",
            "3, 3, 9"
    })
    public void test(int a, int b, int expected) {
        assertEquals(expected, a + b, "failed for: " + expected);
    }

}
