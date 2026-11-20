package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.sl.In;
import runner.Runner;
import utilities.DummyUtilities;

public class Hooks {
    public  static ThreadLocal<Integer> count = ThreadLocal.withInitial(()->0);


    @Before("@Regression")
    public void beforeMethod(Scenario scenario){
        Runner.scenario.set(scenario);
        DummyUtilities.testCaseDescriptorStart();
        count.set(count.get()+1);
    }

    @After("@Regression")
    public  void afterMethod(){
        DummyUtilities.endTest();
        DummyUtilities.flushVariables();
    }
}
