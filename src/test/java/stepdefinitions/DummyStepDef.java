package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modules.DummyModule;

public class DummyStepDef {

    @When("Get One Employee Details")
    public void getOneEmployeeDetails() {
        DummyModule.GetOneEmployeeDetailsRequest();
    }

    @Then("Validate status code {string} and message using {string}")
    public void validateStatusCodeAndMessageUsing(String testcase, String statusCode) {

    }
}
