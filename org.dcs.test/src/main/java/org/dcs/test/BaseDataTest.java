package org.dcs.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by cmathew on 27/01/16.
 */
public class BaseDataTest {

  protected static String dataHomeAbsolutePath;
  protected static String dataInputAbsolutePath;

  @BeforeClass
  public static void baseDataSetup() {
    dataHomeAbsolutePath = DataUtils.getDataHomeAbsolutePath(BaseDataTest.class);
    dataInputAbsolutePath = DataUtils.getDataInputAbsolutePath(BaseDataTest.class);
  }

}
