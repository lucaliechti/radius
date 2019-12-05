package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.User;
import radius.data.dto.EmailDto;
import radius.data.dto.PasswordUuidDto;
import radius.web.components.ModelDecorator;
import radius.web.service.PasswordService;
import radius.web.service.UserService;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class PasswordController {

    private UserService userService;
    private PasswordService passwordService;
    private ModelDecorator modelDecorator;

    public PasswordController(UserService userService, PasswordService passwordService, ModelDecorator modelDecorator) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.modelDecorator = modelDecorator;
    }

    @RequestMapping(value = "/forgot", method=GET)
    public String forgot(@RequestParam(value = "uuid", required = false) String uuid, Model model) {
        if(uuid == null) {
            model.addAttribute("emailForm", new EmailDto());
            return "forgot";
        } else {
            model.addAttribute("uuid", uuid);
            model.addAttribute("passwordForm", new PasswordUuidDto());
            return "reset";
        }
    }

    @RequestMapping(value = "/reset", method=GET)
    public String reset(Model model) {
        model.addAllAttributes(modelDecorator.homeAttributes());
        return "home";
    }

    @RequestMapping(value = "/forgot", method=POST)
    public String forgotten(@ModelAttribute("emailForm") @Valid EmailDto emailForm, BindingResult result, Model model,
                            Locale locale) {
        if(result.hasErrors()) {
            return "forgot";
        }
        String email = emailForm.getEmail();
        Optional<String> uuid = userService.findUuidByEmail(email);
        if(uuid.isPresent()) {
            try {
                passwordService.sendPasswordForgottenEmail(email, uuid.get(), locale);
            } catch (Exception e) {
                model.addAttribute("error", Boolean.TRUE);
            }
        }
        model.addAttribute("sentIfExists", Boolean.TRUE);
        return "forgot";
    }

    @RequestMapping(value = "/reset", method=POST)
    public String reset(@ModelAttribute("passwordForm") @Valid PasswordUuidDto dto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "reset";
        }
        Optional<String> optionalEmail = userService.findEmailByUuid(dto.getUuid());
        if(optionalEmail.isEmpty()) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes());
            return "home";
        }
        Optional<User> optionalUser = userService.findUserByEmail(optionalEmail.get());
        if(optionalUser.isPresent()) {
            boolean success = passwordService.updatePassword(optionalUser.get(), dto.getPassword());
            if (success) {
                model.addAttribute("passwordReset", Boolean.TRUE);
                model.addAllAttributes(modelDecorator.homeAttributes());
                return "home";
            }
        }
        model.addAttribute("generic_error", Boolean.TRUE);
        model.addAllAttributes(modelDecorator.homeAttributes());
        return "home";
    }
}
