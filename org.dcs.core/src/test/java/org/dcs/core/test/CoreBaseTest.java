package org.dcs.core.test;

import java.util.List;

import javax.inject.Inject;

import org.dcs.data.config.ConfigurationFacade;
import org.dcs.data.config.DataConfiguration;
import org.dcs.data.FileDataManager;
import org.dcs.data.impl.FileDataManagerImpl;
import org.dcs.data.impl.LocalDataAdmin;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;



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
            .addClass(LocalDataAdmin.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }
  

}
