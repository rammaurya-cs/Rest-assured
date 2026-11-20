package utilities;

import io.cucumber.java.Scenario;

public class GlobalVariables {
    public static ThreadLocal<String> reportPath = new ThreadLocal<>();
    public static ThreadLocal<Scenario> scenarioName = new ThreadLocal<>();
    public static final ThreadLocal<String> threadCurl = new ThreadLocal<>();
    public static final Object LOCK = new Object();

}
