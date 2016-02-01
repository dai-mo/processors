package org.dcs.api;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by cmathew on 29/01/16.
 */

public interface Configurator {

  public Configuration loadConfiguration();
}
