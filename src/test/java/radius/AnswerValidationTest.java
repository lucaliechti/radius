package radius;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import radius.config.MessageConfig;
import radius.data.form.AnswerForm;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.data.form"})
@Import(MessageConfig.class)
public class AnswerValidationTest {

    @Autowired
    Validator validator;

    @Test
    public void emptyForm() {
        AnswerForm form = new AnswerForm();

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertFalse(invalidProperties.isEmpty());
        assertEquals(2, invalidProperties.size());
        assertTrue(invalidProperties.contains("languages"));
        assertTrue(invalidProperties.contains("locations"));
    }

    @Test
    public void everythingCorrect() {
        AnswerForm form = new AnswerForm();
        form.setLanguages(Arrays.asList("DE", "FR"));
        form.setLocations("1;2;3");
        form.setMotivation("Some motivation");
        form.setRegularanswers(Arrays.asList("TRUE", "FALSE"));
        form.setSpecialanswers(Arrays.asList("FALSE", "TRUE"));

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertTrue(invalidProperties.isEmpty());
    }

    private List<String> getInvalidPropertiesAsList(Set<ConstraintViolation<AnswerForm>> violations) {
        return violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toList());
    }
}
