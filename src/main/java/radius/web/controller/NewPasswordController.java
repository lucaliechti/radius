package radius.web.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.repository.JDBCUserRepository;
import radius.data.dto.PasswordUuidDto;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/reset")
@Controller
public class NewPasswordController {

    private PasswordEncoder encoder;
    private JDBCUserRepository userRepo;
    private HomeController h;

    public NewPasswordController(PasswordEncoder encoder, JDBCUserRepository userRepo, HomeController h) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.h = h;
    }

    @RequestMapping(method=GET)
    public String reset(Model model) {
        return h.cleanlyHome(model);
    }

    @RequestMapping(method=POST)
    public String reset(@ModelAttribute("passwordForm") @Valid PasswordUuidDto dto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            System.out.println("ResetEmailController: Bad PW");
            return "reset";
        }
        try {
            String email = userRepo.findEmailByUuid(dto.getUuid());
            userRepo.updatePassword(encoder.encode(dto.getPassword()), UUID.randomUUID().toString(), email);
        } catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            return h.cleanlyHome(model);
        }
        model.addAttribute("passwordReset", Boolean.TRUE);
        return h.cleanlyHome(model);
    }
}
