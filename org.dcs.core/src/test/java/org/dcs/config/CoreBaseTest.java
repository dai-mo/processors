package org.dcs.config;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;

import org.dcs.api.data.FileDataManager;
import org.dcs.api.data.impl.DataAdmin;
import org.dcs.api.data.impl.FileDataManagerImpl;
import org.dcs.api.utils.DataManagerUtils;
import org.dcs.config.ConfigurationFacade;
import org.dcs.config.DataConfiguration;
import org.dcs.test.DataUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;

/**
 * Created by cmathew on 01/02/16.
 */
public class CoreBaseTest {

  
  protected static DataConfiguration dataConfiguration = ConfigurationFacade.getCurrentDataConfiguration();

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
            .addClass(DataAdmin.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }
  

}
