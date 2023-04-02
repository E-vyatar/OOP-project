package server.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import server.AdminService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MainControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private MainController mainController;

    private MockitoSession mockito;

    @BeforeEach
    public void setup() {
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
    }
    @Test
    public void testBadPassword() {
        when(adminService.isValidPassword("password")).thenReturn(false);

        var res = mainController.authenticate("password");

        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode(),
                "bad password should return http status unauthorized");
    }

    @Test
    public void testCorrectPassword() {
        when(adminService.isValidPassword("password")).thenReturn(true);

        var res = mainController.authenticate("password");

        assertEquals(HttpStatus.ACCEPTED, res.getStatusCode(),
                "correct password should return http status ACCEPTED");
    }

    @AfterEach
    public void tearDown() {
        mockito.finishMocking();
    }
}
