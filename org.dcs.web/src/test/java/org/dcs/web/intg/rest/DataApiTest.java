package org.dcs.web.intg.rest;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.dcs.test.DataUtils;
import org.dcs.web.intg.IntegrationTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

/**
 * Created by cmathew on 28/01/16.
 */
@Category(IntegrationTest.class)
public class DataApiTest {

  @BeforeClass
  public static void setup() {
    RestAssured.port = 8080;
  }


  @Test
  public void testDataUpload() {
    String inputFilePath = DataUtils.getDataInputFileAbsolutePath(this.getClass(), "/test.csv");
    Response response = given().multiPart(new File(inputFilePath)).when().post("/dcs/api/v0/data");
    response.body().prettyPrint();
    response.then().body("errorCode", equalTo("DCS101"));
  }
}
