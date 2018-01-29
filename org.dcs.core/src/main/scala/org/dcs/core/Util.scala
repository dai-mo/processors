package org.dcs.core

import org.dcs.commons.config.Configurator
import org.dcs.commons.serde.YamlSerializerImplicits._

import scala.beans.BeanProperty

/**
  * @author cmathew
  */

case class CoreConfig(@BeanProperty dcsSparkJar: String) {
  def this() = this("")
}

object Util {
  val CoreConfigKey = "config"
  val config: CoreConfig = Configurator(CoreConfigKey).config().toObject[CoreConfig]
}
