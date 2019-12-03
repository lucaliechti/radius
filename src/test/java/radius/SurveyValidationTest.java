package radius;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import radius.config.MessageConfig;
import radius.data.form.SurveyForm;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.data.form"})
@Import(MessageConfig.class)
public class SurveyValidationTest {

    private static String[] correctAnswerArray = new String[15];
    private static List<String> correctAnswers;
    private static List<Integer> correctQuestions = Arrays.asList(1, 15);

    @Autowired
    Validator validator;

    @BeforeClass
    public static void init() {
        correctAnswerArray[0] = "TRUE";
        correctAnswerArray[14] = "FALSE";
        correctAnswers = Arrays.asList(correctAnswerArray);
    }

    @Test
    public void onlyAnswers() {
        SurveyForm form = new SurveyForm();
        form.setAnswers(correctAnswers);

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertFalse(invalidProperties.isEmpty());
        assertEquals(1, invalidProperties.size());
        assertTrue(invalidProperties.contains("questions"));
    }

    @Test
    public void onlyQuestions() {
        SurveyForm form = new SurveyForm();
        form.setQuestions(correctQuestions);

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));
        assertTrue(invalidProperties.isEmpty());
    }

    @Test
    public void tooManyQuestions() {
        SurveyForm form = new SurveyForm();
        form.setQuestions(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertFalse(invalidProperties.isEmpty());
        assertEquals(1, invalidProperties.size());
        assertTrue(invalidProperties.contains("questions"));
    }

    @Test
    public void emptyQuestions() {
        SurveyForm form = new SurveyForm();
        form.setQuestions(new ArrayList<Integer>());

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertFalse(invalidProperties.isEmpty());
        assertEquals(1, invalidProperties.size());
        assertTrue(invalidProperties.contains("questions"));
    }

    @Test
    public void wrongNewsletterFields() {
        SurveyForm form = new SurveyForm();
        form.setQuestions(correctQuestions);
        form.setNewsletter(true);

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));
        assertEquals(1, invalidProperties.size());
        assertTrue(invalidProperties.contains("emailN"));

        form.setEmailN("invalidEmailAddress");

        invalidProperties = getInvalidPropertiesAsList(validator.validate(form));
        assertEquals(1, invalidProperties.size());
        assertTrue(invalidProperties.contains("emailN"));

        form.setEmailN("valid@email.com");

        invalidProperties = getInvalidPropertiesAsList(validator.validate(form));
        assertTrue(invalidProperties.isEmpty());
    }

    @Test
    public void wrongRegistrationDetails() {
        SurveyForm form = new SurveyForm();
        form.setQuestions(correctQuestions);
        form.setRegistration(true);

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertEquals(4, invalidProperties.size());
        assertTrue(invalidProperties.contains("emailR"));
        assertTrue(invalidProperties.contains("firstName"));
        assertTrue(invalidProperties.contains("lastName"));
        assertTrue(invalidProperties.contains("password"));

        form.setFirstName("first");
        form.setLastName("last");

        invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertEquals(2, invalidProperties.size());
        assertTrue(invalidProperties.contains("emailR"));
        assertTrue(invalidProperties.contains("password"));

        form.setPassword("short");

        invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertEquals(2, invalidProperties.size());
        assertTrue(invalidProperties.contains("emailR"));
        assertTrue(invalidProperties.contains("password"));

        form.setEmailR("some@email.com");
        form.setPassword("veryveryveryVERYLONG");

        invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertTrue(invalidProperties.isEmpty());
    }

    @Test
    public void wrongNewsletterAndRegistrationDetails() {
        SurveyForm form = new SurveyForm();
        form.setQuestions(correctQuestions);
        form.setNewsletter(true);
        form.setRegistration(true);

        List<String> invalidProperties = getInvalidPropertiesAsList(validator.validate(form));

        assertEquals(5, invalidProperties.size());
    }

    private List<String> getInvalidPropertiesAsList(Set<ConstraintViolation<SurveyForm>> violations) {
        return violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toList());
    }
}
