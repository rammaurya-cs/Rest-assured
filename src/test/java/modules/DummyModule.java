package modules;

import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;


public class DummyModule {

    public static Response rs;

    public static void getOneEmployeeDetailsRequest() {
        rs = given()
                .header("Content-Type", "application/json")
                .when()
                .get("https://dummy.restapiexample.com/api/v1/employee/1")
                .then()
                .extract().response();
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
