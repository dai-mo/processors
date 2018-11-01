/*
 * Copyright (c) 2017-2018 brewlabs SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
