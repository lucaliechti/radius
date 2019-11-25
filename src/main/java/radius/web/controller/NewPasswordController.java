package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.JDBCUserRepository;
import radius.data.form.passwordUuidDto;
import radius.data.JDBCStaticResourceRepository;

import javax.validation.Valid;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/reset")
@Controller
public class NewPasswordController {

    @Autowired
    private JDBCStaticResourceRepository staticRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JDBCUserRepository userRepo;

    @Autowired
    private HomeController h;

    @RequestMapping(method=GET)
    public String reset(Model model) {
        return h.cleanlyHome(model);
    }

    @RequestMapping(method=POST)
    public String reset(@ModelAttribute("passwordForm") @Valid passwordUuidDto passwordForm, BindingResult result, Model model, Locale locale) {
        if(result.hasErrors()) {
            System.out.println("ResetEmailController: Bad PW");
            return "reset";
        }

        try {
            String email = userRepo.findUserByUuid(passwordForm.getUuid()).getEmail();
            userRepo.updatePassword(encoder.encode(passwordForm.getPassword()), UUID.randomUUID().toString(), email);
        }
        catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            return h.cleanlyHome(model);
        }

        model.addAttribute("passwordReset", Boolean.TRUE);
        return h.cleanlyHome(model);
    }
}
