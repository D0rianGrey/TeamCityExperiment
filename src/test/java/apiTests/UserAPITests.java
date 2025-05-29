package apiTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserAPITests {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void getAllUsers() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("$", not(empty())) // Assert that the response body is not an empty array
            .body("size()", greaterThan(0)) // Assert that the array has more than 0 elements
            .body("[0].id", notNullValue()) // Assert first user has an id
            .body("[0].name", notNullValue()) // Assert first user has a name
            .body("[0].username", notNullValue()) // Assert first user has a username
            .body("[0].email", notNullValue()); // Assert first user has an email
    }

    @Test
    public void getSingleUser() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", equalTo("Leanne Graham"))
            .body("username", equalTo("Bret"))
            .body("email", equalTo("Sincere@april.biz"));
    }
}
