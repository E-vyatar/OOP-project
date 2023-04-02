package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class AdminService {

    private String password;
    private Logger logger = LoggerFactory.getLogger(AdminService.class);

    /**
     * Create an instance of AdminService. Generates the password.
     */
    public AdminService() {
        password = randomAlphabetic(12);
        logger.info("Admin key: " + password);
    }

    /**
     * Checks whether password is valdi.
     * @param password the password to be checked
     * @return whether they're valid
     */
    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Get the password
     * @return password
     */
    public String getPassword() {
        return this.password;
    }
}
