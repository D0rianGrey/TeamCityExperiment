package testRail;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;

public class Utils {
    private static PreemptiveBasicAuthScheme connect() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("vakerin1991@gmail.com");
        authScheme.setPassword("Q852456357159q?");

        return authScheme;
    }

    public static void setBaseSettings() {
        RestAssured.baseURI = "https://vakerin.testrail.io/";
        RestAssured.authentication = connect();
    }

    public static String getPlan(int planId, String parameter) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/index.php?/api/v2/get_plan/" + planId)
                .then()
                .extract()
                .jsonPath()
                .get(parameter)
                .toString();

    }

    public static String getPlan(int planId) {
        return getPlan(planId, "");

    }
}
