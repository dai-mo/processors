package org.dcs.core.module.flow

import java.util.Properties
import org.osgi.framework.BundleContext
import java.util.Map

trait FlowModule {

  def init( bundleContext: BundleContext)
  
	def getPropertyDescriptors(): Map[String, Properties]
	
	def getRelationships(): Map[String, Properties]
	
	def schedule()
	
	def trigger(properties: Properties): Array[Byte]
	
	def unschedule()
	
	def stop()
	
	def shutdown()
	
	def remove()

}
