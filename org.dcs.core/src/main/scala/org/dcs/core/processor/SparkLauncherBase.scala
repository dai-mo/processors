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

package org.dcs.core.processor

import java.io.File
import java.util.{List => JavaList, Map => JavaMap}

import org.apache.spark.launcher.{SparkAppHandle, SparkLauncher}
import org.dcs.api.Constants
import org.dcs.api.processor._
import org.dcs.core.{BuildInfo, Util}

import scala.collection.JavaConverters._

trait SparkLauncherBase extends StatefulRemoteProcessor {

  private var sparkHandle: SparkAppHandle = _
  private var sparkLauncher: SparkLauncher = _

  val SPARK_HOME = "SPARK_HOME"


  def appendSparkPrefix(property: String): String = {
    Constants.SparkPrefix + property
  }

  override def initState(): Unit = {
    // NOTE: This requires the SPARK_HOME to be set
    //       and the dcs spark jar be copied to the
    //       SPARK_HOME dir
    val sparkHome = sys.env(SPARK_HOME)
    val dcsSparkJar = Util.config.dcsSparkJar
    sparkLauncher = new SparkLauncher()
      .setAppResource(sparkHome + File.separator + "dcs" + File.separator + dcsSparkJar)
      .setMainClass("org.dcs.spark.processor." + this.getClass.getSimpleName + "Job")
      .setMaster(Constants.DefaultMaster)
      .setAppName(Constants.DefaultAppName)
      .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
  }

  override def onSchedule(propertyValues: JavaMap[RemoteProperty, String]): Boolean = {
    if(sparkHandle == null) {
      val receiver = propertyValues.asScala.find(_._1.getName == ExternalProcessorProperties.ReceiverKey).map(_._2)
      val sender = propertyValues.asScala.find(_._1.getName == ExternalProcessorProperties.SenderKey).map(_._2)
      val readSchemaId = propertyValues.asScala.find(_._1.getName == CoreProperties.ReadSchemaIdKey).map(_._2)

      // FIXME: Currently passing properties as spark conf,
      //        hence the prefixing of "spark." string.
      //        Is there a better way to do this ?
      if (receiver.isDefined && sender.isDefined && readSchemaId.isDefined) {
        propertyValues.asScala
          //.map(pv => (pv._1, if(pv._2 == null) pv._1.defaultValue else pv._2))
          .filter(_._2 != null).foreach(pv =>
          sparkLauncher = sparkLauncher.setConf(appendSparkPrefix(pv._1.getName()), pv._2)
        )
        sparkLauncher = sparkLauncher
          .setConf(appendSparkPrefix(CoreProperties.ProcessorClassKey), this.getClass.getName)
          .setConf(appendSparkPrefix(CoreProperties.SchemaIdKey), schemaId)
        sparkHandle = sparkLauncher.startApplication()
        true
      } else
        throw new IllegalArgumentException("One of receiver, sender or read schema id is not set")
    } else
      true

  }

  override def onStop(propertyValues: JavaMap[RemoteProperty, String]): Boolean = {
    onRemove(propertyValues)
  }

  override def onShutdown(propertyValues: JavaMap[RemoteProperty, String]): Boolean = {
    onRemove(propertyValues)
  }

  override def onRemove(propertyValues: JavaMap[RemoteProperty, String]): Boolean = {
    if(sparkHandle != null && sparkHandle.getState == SparkAppHandle.State.RUNNING) {
      // FIXME: Need to figure out why stop does not work
      // sparkHandle.stop()
      sparkHandle.kill()
      sparkHandle = null
    }
    true
  }

}
