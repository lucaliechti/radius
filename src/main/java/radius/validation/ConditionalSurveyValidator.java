package radius.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class ConditionalSurveyValidator implements ConstraintValidator<Conditional, Object> {

    private String selected;
    private String[] required;
    private String[] email;
    private String[] pw;
    private String message;
    private String[] values;
    private final int MIN_SIZE_PW = 8;
    private EmailValidator emailValidator;

    @Override
    public void initialize(Conditional requiredIfChecked) {
        selected = requiredIfChecked.selected();
        required = requiredIfChecked.required();
        email = requiredIfChecked.email();
        pw = requiredIfChecked.pw();
        message = requiredIfChecked.message();
        values = requiredIfChecked.values();
        emailValidator = EmailValidator.getInstance();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Boolean valid = true;
        try {
            Object checkedValue = BeanUtils.getProperty(object, selected);
            if (Arrays.asList(values).contains(checkedValue)) {

                //check required values
                for (String propName : required) {
                    Object requiredValue = BeanUtils.getProperty(object, propName);
                    valid = requiredValue != null && !isEmpty(requiredValue);
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("error.notBlank").addPropertyNode(propName).addConstraintViolation();
                    }
                }

                //check emails
                for (String propName : email) {
                    String requiredValue = BeanUtils.getProperty(object, propName);
                    valid = requiredValue != null && !isEmpty(requiredValue) && emailValidator.isValid(requiredValue);
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("{error.email}").addPropertyNode(propName).addConstraintViolation();
                    }
                }

                //check passwords
                for (String propName : pw) {
                    Object requiredValue = BeanUtils.getProperty(object, propName);
                    valid = requiredValue != null && !isEmpty(requiredValue) && ((String) requiredValue).length() >= MIN_SIZE_PW;
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("{error.sizePW}").addPropertyNode(propName).addConstraintViolation();
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Accessor method is not available for class: " + object.getClass().getName() + ", exception: " + e);
            return false;
        } catch (NoSuchMethodException e) {
            log.error("Field or method is not present on class: " + object.getClass().getName() + ", exception: " + e);
            return false;
        } catch (InvocationTargetException e) {
            log.error("An exception occurred while accessing class: " + object.getClass().getName() + ", exception: " + e);
            return false;
        }
        return valid;
    }
}
