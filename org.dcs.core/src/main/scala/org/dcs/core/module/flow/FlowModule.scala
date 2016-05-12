package org.dcs.core.module.flow

import java.util.Properties
import org.osgi.framework.BundleContext

import java.util.{Map => JavaMap}


trait FlowModule {

  def init( bundleContext: BundleContext)
  
	def getPropertyDescriptors(): JavaMap[String, JavaMap[String, String]]
	
	def getRelationships(): JavaMap[String, JavaMap[String, String]]
	
	def schedule()
	
	def trigger(properties: JavaMap[String, String]): Array[Byte]
	
	def unschedule()
	
	def stop()
	
	def shutdown()
	
	def remove()

}
