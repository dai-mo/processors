package org.dcs.core.test;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CoreMockFactory {

  public static FormDataContentDisposition getFormDataContentDisposition(final String filePath) {

  	FormDataContentDisposition fdcd = mock(FormDataContentDisposition.class);
    when(fdcd.getFileName()).thenAnswer(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        return filePath;
      }
    });
    return fdcd;
  }
}
