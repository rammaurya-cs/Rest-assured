package utilities;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();
    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("validation/api-validation.properties")) {
            if (input == null) {
                throw new RuntimeException("api-validation.properties file not found");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load API validation properties", e);
        }
    }

    public static String getExpectedMessage(String testcase) {
        String expectedMessage = properties.getProperty(testcase);
        if (expectedMessage == null || expectedMessage.trim().isEmpty()) {
            throw new RuntimeException(
                    "Expected message not configured for testcase: " + testcase
            );
        }
        return expectedMessage.trim();
    }
}