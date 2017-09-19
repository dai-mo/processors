package org.dcs.core.processor

import java.io.File
import java.util
import java.util.{List => JavaList, Map => JavaMap}

import org.apache.spark.launcher.{SparkAppHandle, SparkLauncher}
import org.dcs.api.Constants
import org.dcs.api.processor.CoreProperties.remoteProperty
import org.dcs.api.processor._
import org.dcs.core.BuildInfo

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
    val dcsSparkJar = "org.dcs.spark-" + BuildInfo.version + "-assembly.jar"
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

  override def properties(): JavaList[RemoteProperty] = {
    val props = new util.ArrayList(super.properties())


    val receiverProperty =  remoteProperty(ExternalProcessorProperties.ReceiverKey,
      "Id of receiver for external processor [Level" + PropertyLevel.Open + "]",
      "",
      isRequired = true,
      isDynamic = false,
      PropertyLevel.Open)
    props.add(receiverProperty)

    val rootInputPortProperty =  remoteProperty(ExternalProcessorProperties.RootInputConnectionKey,
      "Id of root input port [Level" + PropertyLevel.Open + "]",
      "",
      isRequired = true,
      isDynamic = false,
      PropertyLevel.Open)
    props.add(rootInputPortProperty)

    val senderProperty =  remoteProperty(ExternalProcessorProperties.SenderKey,
      "Id of sender for external processor [Level" + PropertyLevel.Open + "]",
      "",
      isRequired = true,
      isDynamic = false,
      PropertyLevel.Open)
    props.add(senderProperty)

    val rootOutputPortProperty =  remoteProperty(ExternalProcessorProperties.RootOutputConnectionKey,
      "Id of root output port [Level" + PropertyLevel.Open + "]",
      "",
      isRequired = true,
      isDynamic = false,
      PropertyLevel.Open)
    props.add(rootOutputPortProperty)

    props
  }

}
