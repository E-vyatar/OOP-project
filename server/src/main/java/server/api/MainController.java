package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.AdminService;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private AdminService adminService;

    /**
     * Method from old code that has to be changed
     *
     * @return "Hello World"
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello world!";
    }

    /**
     * Check whether the given password is a valid admin password
     * @param password the given password
     * @return the HTTP response. Has status code 202 if password is valid,
     *      401 if it's unvalid.
     */
    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestHeader String password) {
        HttpStatus status;
        if (adminService.isValidPassword(password)){
            status = HttpStatus.ACCEPTED;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity(status);
    }

}