package utilities;

import org.testng.Assert;

import static modules.DummyModule.rs;

public class DummyUtilities {
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
