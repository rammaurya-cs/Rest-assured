package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import runner.TestRunner;
import utilities.DummyUtilities;

public class Hooks {
    public  static ThreadLocal<Integer> count = ThreadLocal.withInitial(()->0);


    @Before("@Regression")
    public void beforeMethod(Scenario scenario){
        TestRunner.scenario.set(scenario);
        DummyUtilities.testCaseDescriptorStart();
        count.set(count.get()+1);
    }

    @After("@Regression")
    public  void afterMethod(){
        DummyUtilities.endTest();
        DummyUtilities.flushVariables();
    }
}
