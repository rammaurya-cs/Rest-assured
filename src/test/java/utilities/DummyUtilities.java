package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.Scenario;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import runner.TestRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static modules.DummyModule.rs;
import static runner.TestRunner.logger;

public class DummyUtilities {

    //    public void BaseURI() {
//        RestAssured.baseURI = "https://dummy.restapiexample.com";
//    }
    public static String getBaseURI() {
        return "https://dummy.restapiexample.com";
    }

    public static Headers setHeader(String[] keys, String[] values) {
        if (keys == null || values == null || keys.length != values.length) {
            throw new IllegalArgumentException("Header keys and values must have equal length");
        }
        Header[] headers = new Header[keys.length];
        for (int i = 0; i < keys.length; i++) {
            headers[i] = new Header(keys[i], values[i]);
        }
        return new Headers(headers);

    }

    public static Response sendRequest(String httpMethod, String endpoint, Headers headers, ObjectNode payLoad, Object[] queryParams, Object[] queryValues, String title) {
        try {
            // Create request specification
            RequestSpecification request = given();
            String APIFullPath = getBaseURI() + endpoint;
            System.out.println(APIFullPath);
            // 1. Add headers
            if (headers != null) {
                request.headers(headers);
            }
            // 2. Add query parameters
            if (queryParams != null && queryValues != null) {
                if (queryParams.length != queryValues.length) {
                    throw new IllegalArgumentException("Query parameters and values must have equal length");
                }
                for (int i = 0; i < queryParams.length; i++) {
                    request.queryParam(queryParams[i].toString(), queryValues[i]);
                }
            }
            // 3. Add request payload
            if (payLoad != null) {
                request.body(payLoad);
            }
            // 4. Execute API request
            Response response = request.when().request(httpMethod.toUpperCase(), APIFullPath);
            // 5. Print request and response details
            System.out.println("========== " + title + " ==========");
            System.out.println("HTTP Method: " + httpMethod);
            System.out.println("Endpoint: " + endpoint);

            //log
            logger.get().log(Status.INFO, String.format("<b>%s Endpoint : </b>%s", title, APIFullPath));
            if (headers != null) {
                logger.get().log(Status.INFO, "<b>Request Headers : </b>" + headers.asList());
            }
            if (payLoad != null && !httpMethod.equalsIgnoreCase("GET")) {
                logger.get().log(Status.INFO, "<b> Request Payload : </b><pre>" + payLoad.toPrettyString() + "</pre>");
            }

            logger.get().log(Status.INFO, String.format("<b>%s Response : </b><pre>%s</pre>", title, response.asPrettyString()));

            //curl Builder
            StringBuilder cURL = new StringBuilder("cURL -X ")
                    .append(httpMethod.toUpperCase())
                    .append(" '")
                    .append(APIFullPath).append("'");

            if (headers != null) {
                headers.asList().forEach(h -> cURL.append(" -H '").append(h.getName()).append(": ").append(h.getValue()).append("'"));
            }
            if (payLoad != null && !httpMethod.equalsIgnoreCase("GET")) {
                cURL.append(" -d '").append(payLoad.toString().replace("'", "\\'")).append("'");
            }
            GlobalVariables.threadCurl.set(cURL.toString());
            response.then().log().all();
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Request failed " + e.getMessage());
        }
    }


    public static void validateStatusCodeAndMessage(int statusCode, String testcase) {
        try {
            // Status code validation from feature file
            int actualStatusCode = rs.getStatusCode();
            Assert.assertEquals(actualStatusCode, statusCode);
            logger.get().log(Status.PASS, "Status Code Validation Passed | Expected: " + statusCode + " | Actual: " + actualStatusCode);

            // Status message validation from properties file
            String expectedMessage = ConfigReader.getExpectedMessage(testcase);
            String actualMessage = rs.jsonPath().getString("message");
            Assert.assertTrue(actualMessage.startsWith(expectedMessage), "Status Message Mismatch | Expected to start with: " + expectedMessage + " | Actual: " + actualMessage);
            logger.get().log(Status.PASS, "Status Message Validation Passed | Expected: " + expectedMessage + " | Actual: " + actualMessage);
        } catch (AssertionError e) {
            logger.get().log(Status.FAIL, "<b>API Validation Failed:</b> " + e.getMessage());
            throw e;
        }
    }


    //=====================
    public static void testCaseDescriptorStart() {
        Scenario scenario = TestRunner.scenario.get();
        GlobalVariables.scenarioName.set(scenario);

        String testCaseDescription = scenario.getName();
        String testcaseName = testCaseDescription.replaceAll("\\s", "");
        System.out.println("Test Case Name " + testcaseName);

        String tags = String.join(",", scenario.getSourceTagNames());
        String newName = scenario.getName() + "[" + tags + "]";
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("testName", newName);

        ExtentReports extentReports = TestRunner.getExtentReports();
        ExtentTest testLogger = extentReports.createTest(testCaseDescription);
        TestRunner.setLogger(testLogger);

        try {
            Collection<String> tagList = scenario.getSourceTagNames();
            if (!tagList.isEmpty()) {
                for (String tag : tagList) {
                    TestRunner.getLogger().assignCategory(tag);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        LogUtils.infoLog(TestRunner.class," Suite started at "+ scenario.getName());

    }

    public static void endTest() {
        Scenario scenario = TestRunner.getScenario();
        ExtentTest logger = TestRunner.getLogger();

        if (scenario.isFailed()) {
            if (logger != null) {
                logger.log(Status.FAIL, "<b> Test Case Failed!</b><br>Reproducible with cURL:<br><pre>"
                        + GlobalVariables.threadCurl.get() + "</pre>");
            }
        } else {
            if (logger != null) {
                logger.log(Status.PASS, "Test Case Passed");
            }
        }
    }

    public static void flushVariables() {
        TestRunner.scenario.remove();
        logger.remove();
//        GlobalVariables.payLoad.remove();
//        GlobalVariables.endpoint.remove();
//        GlobalVariables.headers.remove();
//        GlobalVariables.response.remove();
//        GlobalVariables.threadCurl.remove();
        if (TestRunner.extentReports.get() != null) {
            TestRunner.extentReports.get().flush();
        }
    }

    public static void clearDirectory() {

        synchronized (GlobalVariables.LOCK) {
            try {
                GlobalVariables.reportPath.set(System.getProperty("user.dir") + File.separator + "Reports" + File.separator
                        + "HTMLReports");
                File reportDir = new File(GlobalVariables.reportPath.get());
                if (reportDir.exists()) {
                    System.out.println("[INFO] Cleaning report directory " + reportDir.getAbsolutePath());
                    FileUtils.cleanDirectory(reportDir);
                } else {
                    System.out.println("[INFO] Report dir does not exist " + reportDir.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
