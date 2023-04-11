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
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;

public class MainControllerTest {

    @Mock
    private AdminService adminService;

    private MockMvc mockMvc;

    @InjectMocks
    private MainController mainController;

    private MockitoSession mockito;

    @BeforeEach
    public void setup() {
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
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


    @Test
    void indexTest() throws Exception {
        String expected = "Hello world!";

        String response = mockMvc.perform(get("/")).andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }
}