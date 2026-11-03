package practice;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import  static org.hamcrest.Matchers.*;
import org.testng.annotations.Test;

public class GetExample {
    @Test
    public void getEmployee() {
        //https://dummy.restapiexample.com/api/v1/employee/1
        Response rs = given()
                .header("Content-Type", "application/json")
                .when()
                .get("https://dummy.restapiexample.com/api/v1/employee/1")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.id",equalTo(1))
                .body("data.employee_salary",equalTo(320800))
                .extract().response();

        System.out.println(rs.asString());
    }
}
