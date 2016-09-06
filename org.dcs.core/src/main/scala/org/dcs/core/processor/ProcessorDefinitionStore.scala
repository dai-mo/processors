package org.dcs.core.processor

import org.dcs.api.processor.{ProcessorDefinition, RemoteProcessor}
import org.dcs.api.service.DefinitionStore

/**
  * Created by cmathew on 06/09/16.
  */
trait ProcessorDefinitionStore extends DefinitionStore {

  override def getDef(processor: RemoteProcessor): ProcessorDefinition =
    processor
}
