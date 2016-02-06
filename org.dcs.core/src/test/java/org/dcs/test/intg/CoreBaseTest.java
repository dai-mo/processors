package org.dcs.test.intg;

import java.util.List;

import javax.inject.Inject;

import org.dcs.api.data.FileDataManager;
import org.dcs.api.data.impl.FileDataManagerImpl;
import org.dcs.config.ConfigurationFacade;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Created by cmathew on 01/02/16.
 */
public class CoreBaseTest {


  @Inject
  protected FileDataManager dataManager;


  public static List<Class<?>> getClassesToAdd() {
    return null;
  }


  public static JavaArchive createBaseDeployment() {
    return ShrinkWrap.create(JavaArchive.class)
            .addClass(FileDataManager.class)
            .addClass(FileDataManagerImpl.class)
            .addClass(ConfigurationFacade.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

}
