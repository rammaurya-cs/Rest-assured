package modules;

import io.restassured.response.Response;
import utilities.DummyUtilities;
import utilities.Endpoint;
import utilities.HeaderUtils;

import static io.restassured.RestAssured.given;

public class DummyModule {
    public static Response rs;

    public static void getOneEmployeeDetailsRequest() {
        rs = DummyUtilities.sendRequest("GET", Endpoint.getOneEmployee, HeaderUtils.headerForgetOneEmployee(),null,null,null,"Get One Employee");
    }

    public static void getAllEmployeeDetailsRequest() {
        rs = given()
                .header("Content-Type", "application/json")
                .when()
                .get("https://dummy.restapiexample.com/api/v1/employees")
                .then()
                .extract().response();
    }
}
