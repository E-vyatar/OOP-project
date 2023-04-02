package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {

    private AdminService adminService;

    @BeforeEach
    public void setup(){
        this.adminService = new AdminService();
    }

    @Test
    public void testEmptyPassword() {
        assertFalse(adminService.isValidPassword(""), "an empty password shouldn't be valid");
    }
    @Test
    public void testNullPassword() {
        assertFalse(adminService.isValidPassword(null), "null shouldn't be the valid password");
    }
    @Test
    public void testPassword() {
        String password = adminService.getPassword();
        assertTrue(adminService.isValidPassword(password), "The returned password should be valid");
    }
}
