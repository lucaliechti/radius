package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.PasswordForm;
import radius.User;
import radius.UserForm;
import radius.data.JDBCStaticResourceRepository;
import radius.data.UserRepository;

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
    UserRepository userRepo;

    @RequestMapping(method=GET)
    public String reset(Model model) {
        model.addAttribute("registrationForm", new UserForm());
        model.addAttribute("cantons", staticRepo.cantons());
        return "home";
    }

    @RequestMapping(method=POST)
    public String reset(@ModelAttribute("passwordForm") @Valid PasswordForm passwordForm, BindingResult result, Model model, Locale locale) {
        if(result.hasErrors()) {
            System.out.println("ResetEmailController: Bad PW");
            return "reset";
        }

        try {
            String email = userRepo.findUserByUuid(passwordForm.getUuid()).getEmail();
            userRepo.updatePassword(encoder.encode(passwordForm.getPassword()), UUID.randomUUID().toString(), email);
        }
        catch (Exception e) {
            model.addAttribute("passwordreset_error", Boolean.TRUE);
            model.addAttribute("registrationForm", new UserForm());
            model.addAttribute("cantons", staticRepo.cantons());
            return "home";
        }

        model.addAttribute("passwordReset", Boolean.TRUE);
        model.addAttribute("registrationForm", new UserForm());
        model.addAttribute("cantons", staticRepo.cantons());
        return "home";
    }
}
