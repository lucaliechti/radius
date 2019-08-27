package radius.web.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class RealWorldProperties {

    private boolean specialIsActive;
    private int numberOfVotes;

    public RealWorldProperties() { readFromFile("/config/realworld.properties"); } //hardcoded

    private void readFromFile(String file) {
        Properties p = new Properties();
        InputStream in;
        try {
            in = radius.web.components.RealWorldProperties.class.getResourceAsStream(file);
            p.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            specialIsActive = Boolean.valueOf(p.getProperty("specialIsActive"));
        }
        catch (Exception e) {
            System.out.println("Couldn't decide whether the special is active.");
            specialIsActive = Boolean.TRUE;
        }
        try {
            numberOfVotes = Integer.parseInt(p.getProperty("numberOfVotes"));
        }
        catch (NumberFormatException e) {
            System.out.println("Didn't understand how many votes there are.");
            numberOfVotes = -1;
        }
    }

    public boolean specialIsActive() {
        return specialIsActive;
    }

    public int numberOfVotes() {
        return numberOfVotes;
    }
}
