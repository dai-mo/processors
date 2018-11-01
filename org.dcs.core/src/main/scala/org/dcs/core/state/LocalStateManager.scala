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
