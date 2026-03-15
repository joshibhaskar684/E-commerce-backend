package app.auth.service.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestingController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getchar(){
        return "hello mere bahi sucess h teri ";
    }
}
