package utilities;

import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.baseURI;
public class SpartanTestBase {
    @BeforeAll
    public static void init() {
        baseURI ="http://54.144.232.180:7000/api";

    }
}
