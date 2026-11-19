package utilities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static modules.DummyModule.rs;

public class DummyUtilities {

    public Response sendRequest(String httpMethod, String endpoint, Headers headers, ObjectNode payLoad, Object[] queryParams, Object[] queryValues, String title) {
        // Create request specification
        RequestSpecification request = given();

        // 1. Add headers
        if (headers != null) {
            request.headers(headers);
        }

        // 2. Add query parameters
        if (queryParams != null && queryValues != null) {

            if (queryParams.length != queryValues.length) {
                throw new IllegalArgumentException(
                        "Query parameters and values must have equal length");
            }

            for (int i = 0; i < queryParams.length; i++) {

                request.queryParam(
                        queryParams[i].toString(),
                        queryValues[i]);
            }
        }

        // 3. Add request payload
        if (payLoad != null) {
            request.body(payLoad);
        }

        // 4. Execute API request
        Response response = request
                .when()
                .request(httpMethod.toUpperCase(), endpoint);

        // 5. Print request and response details
        System.out.println("========== " + title + " ==========");
        System.out.println("HTTP Method: " + httpMethod);
        System.out.println("Endpoint: " + endpoint);

        response.then().log().all();

        return response;
    }


    public static void validateStatusCodeAndMessage(int statusCode, String testcase) {
        Assert.assertEquals(rs.getStatusCode(), statusCode);
        Assert.assertEquals(rs.jsonPath().getString("status"), "success");
        if (testcase.equalsIgnoreCase("GetOneEmployeeDetails")) {
            Assert.assertEquals(rs.jsonPath().getInt("data.id"), 1);
        } else {
            Assert.assertEquals(rs.jsonPath().getInt("data[0].employee_salary"), 320800);
            Assert.assertEquals(rs.jsonPath().getInt("data[0].employee_age"), 61);
        }
    }
}
