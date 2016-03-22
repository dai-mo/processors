package org.dcs.core.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.components.PropertyValue;
import org.apache.nifi.processor.ProcessContext;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CoreMockFactory {

  public static ProcessContext getMockProcessContext(PropertyDescriptor pd, PropertyValue pv){

  	ProcessContext processContext = mock(ProcessContext.class);
    when(processContext.getProperty(pd)).thenAnswer(new Answer<PropertyValue>() {     

      @Override
      public PropertyValue answer(InvocationOnMock invocation) throws Throwable {
      	return pv;
      }
    });
    return processContext;
  }
  
  public static PropertyValue getMockPropertyValue(String value){

  	PropertyValue propertyValue = mock(PropertyValue.class);
    when(propertyValue.getValue()).thenAnswer(new Answer<String>() {     

      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
      	return value;
      }
    });
    return propertyValue;
  }

}
