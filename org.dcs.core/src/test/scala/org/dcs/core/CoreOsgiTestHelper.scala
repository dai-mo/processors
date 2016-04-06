package org.dcs.core

import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.JavaArchive
import org.jboss.shrinkwrap.api.asset.EmptyAsset

object CoreOsgiTestHelper {
  
 def createBaseDeployment():JavaArchive = {
    return ShrinkWrap.create(classOf[JavaArchive])
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }
}