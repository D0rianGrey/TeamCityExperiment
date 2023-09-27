package testRail;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;

import static testRail.Utils.getPlan;
import static testRail.Utils.setBaseSettings;

public class Runner {
    public static void main(String[] args) {

        setBaseSettings();
        System.out.println(getPlan(2, "url"));
    }
}
