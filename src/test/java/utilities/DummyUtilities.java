package utilities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apiguardian.api.API;
import org.testng.Assert;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static modules.DummyModule.rs;

public class DummyUtilities {

//    public void BaseURI() {
//        RestAssured.baseURI = "https://dummy.restapiexample.com";
//    }
    public static String getBaseURI() {
        return "https://dummy.restapiexample.com";
    }

    public static Headers setHeader(String[] keys,String[] values){
        if (keys == null || values == null || keys.length != values.length) {
            throw new IllegalArgumentException(
                    "Header keys and values must have equal length");
        }

        Header[] headers = new Header[keys.length];
        for (int i = 0; i < keys.length; i++) {
            headers[i] = new Header(keys[i], values[i]);
        }

        return new Headers(headers);

    }

    public static Response sendRequest(String httpMethod, String endpoint, Headers headers, ObjectNode payLoad, Object[] queryParams, Object[] queryValues, String title) {
        // Create request specification
        RequestSpecification request = given();
        String APIFullPath = getBaseURI()+endpoint;
        System.out.println(APIFullPath);

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
                .request(httpMethod.toUpperCase(), APIFullPath);

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
