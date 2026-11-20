package runner;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import hooks.Hooks;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utilities.DummyUtilities;
import utilities.ReportUtils;


@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        tags = "@GetOneEmployeeDetails",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json"
        },
        monochrome = true,
        dryRun = false
)
public class Runner extends AbstractTestNGCucumberTests {

    public static ThreadLocal<ExtentReports> extentReports = new ThreadLocal<>();
    public static  ThreadLocal<ExtentTest> logger = new ThreadLocal<>();
    public static  ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    // ExtentReports getter and setter
    public static ExtentReports getExtentReports() {
        return extentReports.get();
    }

    public static void setExtentReports(ExtentReports reports) {
        extentReports.set(reports);
    }

    public static ExtentTest getLogger() {
        return logger.get();
    }

    public static void setLogger(ExtentTest test) {
        logger.set(test);
    }
    public static Scenario getScenario() {
        return scenario.get();
    }

    public static void setScenario(Scenario currentScenario) {
        scenario.set(currentScenario);
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        DummyUtilities.clearDirectory();
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        setExtentReports(ReportUtils.extentReportInitialization());
    }

    @AfterClass(alwaysRun = true)
    public void afterClass(){
        ExtentReports extentReports1= getExtentReports();
        ReportUtils.extentReportClosure(extentReports1);
        ReportUtils.openReport();
        extentReports.remove();
        Hooks.count.remove();
    }

}
