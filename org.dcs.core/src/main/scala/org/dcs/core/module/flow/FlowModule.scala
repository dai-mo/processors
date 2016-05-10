package org.dcs.core.module.flow

import java.util.Properties
import org.osgi.framework.BundleContext


trait FlowModule {

  def init( bundleContext: BundleContext)
  
	def getPropertyDescriptors(): Map[String, Map[String, String]]
	
	def getRelationships(): Map[String, Map[String, String]]
	
	def schedule()
	
	def trigger(properties: Map[String, String]): Array[Byte]
	
	def unschedule()
	
	def stop()
	
	def shutdown()
	
	def remove()

}
