package utilities;
import io.restassured.http.Headers;

import static utilities.DummyUtilities.setHeader;

public class HeaderUtils {

    public static Headers headerForgetOneEmployee(){
        String[] keys = {"Content-Type", "Accept"};
        String[] values = {"application/json", "application/json"};
        return setHeader(keys,values);
    }
}
