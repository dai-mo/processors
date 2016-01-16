package org.dcs.core.service;

import org.dcs.api.ApiResponseMessage;
import org.dcs.api.ModulesApiService;
import org.dcs.api.NotFoundException;
import org.osgi.service.component.annotations.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Component
public class ModulesApiServiceImpl implements ModulesApiService {
      @Override
      public Response modulesGet(List<String> type,SecurityContext securityContext)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
}
