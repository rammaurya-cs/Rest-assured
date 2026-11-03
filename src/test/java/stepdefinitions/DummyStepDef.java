package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static modules.DummyModule.*;
import static utilities.DummyUtilities.*;

public class DummyStepDef {

    @When("Get One Employee Details")
    public void getOneEmployeeDetails() {
        getOneEmployeeDetailsRequest();
    }


    @Then("Validate status code {int} and message using {string}")
    public void validateStatusCodeAndMessageUsing(int statusCode,String testcase) {
        validateStatusCodeAndMessage(statusCode, testcase);
    }

    @When("Get All Employee Details")
    public void getAllEmployeeDetails() {
        getAllEmployeeDetailsRequest();
    }
}
