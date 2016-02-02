package org.dcs.web.intg.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.test.DataUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by cmathew on 28/01/16.
 */

public class DataApiIT {

  @BeforeClass
  public static void setup() {

    RestAssured.port = 8080;
    System.setProperty("mode","test");
  }

  @Before
  public void preTest() {
    String targetDirectory = DataUtils.getTargetDirectory(this.getClass());
    DataManagerUtils.deleteDirContents(new File(targetDirectory + File.separator + "data" + File.separator + DataManagerUtils.DATA_HOME_DIR_NAME));
  }

  @Test
  public void testDataUpload() {
    String inputFilePath = DataUtils.getDataInputFileAbsolutePath(this.getClass(), "/test.csv");
    Response response = given().multiPart(new File(inputFilePath)).when().post("/dcs/api/v0/data");
    response.body().prettyPrint();
    String json = response.asString();
    UUID data_source_id = JsonPath.from(json).getUUID("data_source_id");

    response = given().multiPart(new File(inputFilePath)).when().post("/dcs/api/v0/data");
    response.body().prettyPrint();
    response.then().body("errorCode", equalTo("DCS101"));
  }
}
