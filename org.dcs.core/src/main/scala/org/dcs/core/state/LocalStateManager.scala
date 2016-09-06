package org.dcs.core.state

import java.util.UUID

import org.dcs.api.processor.{StateManager, StatefulRemoteProcessor}

import scala.collection.mutable.Map

/**
  * Created by cmathew on 06/09/16.
  */
trait LocalStateManager extends StateManager {

  val processorStateMap: Map[String, StatefulRemoteProcessor] = Map()

  override def put(processor: StatefulRemoteProcessor): String = {
    val processorStateId = UUID.randomUUID().toString
    processorStateMap += (processorStateId -> processor)
    processorStateId
  }

  override def get(processorStateId: String): Option[StatefulRemoteProcessor] = {
    processorStateMap.get(processorStateId)
  }

  override def remove(processorStateId: String): Boolean = {
    processorStateMap.remove(processorStateId).isDefined
  }

}
