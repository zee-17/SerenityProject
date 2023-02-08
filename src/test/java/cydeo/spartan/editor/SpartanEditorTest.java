package cydeo.spartan.editor;

import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import utilities.SpartanTestBase;
import utilities.SpartanUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorTest extends SpartanTestBase {

    @DisplayName("POST /spartan as editor")
    @Test
    public void postSpartan() {

        Map<String, Object> spartanMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("spartanMap = " + spartanMap);


        given().auth().basic("editor","editor")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(spartanMap). // SERIALIZATION
                when().post("/spartans").prettyPeek();

        /*
        SERIALIZATION  --> JAVA/POJO  to JSON
        DESERILIZATION --> JSON to JAVA/POJO
        Do we need to add dependency?
        - With Regular Rest Assured we were adding JACKSON Databind for DESERILIZATION and SERIALIZATION
        - For Serenity we are not gonna add Jackson / GSON or other databind / objectMapper dependency
         */


        //Status Code
        Ensure.that("Status Code is 201",vRes->vRes.statusCode(201));

        //Content Type
        Ensure.that("Content Type is JSON",vRes->vRes.contentType(ContentType.JSON));
        //A Spartan is Born!
        Ensure.that("Success Mesage is A Spartan is Born!",vRes->vRes.body("success",is("A Spartan is Born!")));
        // id is not null
        Ensure.that("ID is NOT NULL",vRes->vRes.body("data.id",notNullValue()));
        // name is correct
        Ensure.that("Name is Correct",vRes->vRes.body("data.name",is(spartanMap.get("name"))));
        // gender is correct
        Ensure.that("Gender is Correct",vRes->vRes.body("data.gender",is(spartanMap.get("gender"))));
        // phone is correct
        Ensure.that("Phone is correct",vRes-> vRes.body("data.phone",is(spartanMap.get("phone"))));
        //EXTRACT DATA FROM ID
        String id = lastResponse().jsonPath().getString("data.id");
        // check location header ends with newly generated id
        Ensure.that("check location header ends with newly generated id",vRes->vRes.header("Location",endsWith(id)));

    }
    /*
    {index} --> will shown in screen based on number of iteration
    {0} --> it refers name
    {1} --> it refers gender
    {2} --> it refers phone
     */

    @ParameterizedTest(name ="POST SPARTAN {index} name {0}")
    @CsvFileSource(resources = "/SpartanPOST.csv",numLinesToSkip = 1)
    public void POSTSpartan(String name,String gender, long phone){

        System.out.println("name = " + name);
        System.out.println("gender = " + gender);
        System.out.println("phone = " + phone);

        Map<String,Object> spartanMap=new LinkedHashMap<>();
        spartanMap.put("name",name);
        spartanMap.put("gender",gender);
        spartanMap.put("phone",phone);

        System.out.println("spartanMap = " + spartanMap);


        given().auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(spartanMap)
                .when().post("/spartans");

        //status code is 201
        Ensure.that("Status Code is 201", vRes->vRes.statusCode(201));
        //content type is CONTENT TYPE JSON
        Ensure.that("Content Type is JSON",vRes->vRes.contentType(ContentType.JSON));
        // A Spartan is Born!
        Ensure.that("A Spartan is Born!",vRes->vRes.body("success",is("A Spartan is Born!")));


    }

}
