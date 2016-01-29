package org.dcs.api;

import javax.inject.Named;
import java.io.File;
import java.net.URL;

/**
 * Created by cmathew on 29/01/16.
 */
@Named("testConfigurator")
public class TestConfigurator extends YamlConfigurator {

  public TestConfigurator() {
    URL resource = this.getClass().getResource(".");
    config.setDataRootPath(resource.getPath() + config.getDataRoot());
  }
}
