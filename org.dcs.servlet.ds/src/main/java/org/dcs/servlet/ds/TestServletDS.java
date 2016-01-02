package org.dcs.servlet.ds;

import org.osgi.service.component.annotations.*;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by cmathew on 28/12/15.
 */

@Component(
        name = "org.dcs.servlet.ds.TestServletDS",
        immediate = true
)
public class TestServletDS extends HttpServlet {


    private static final String SERVLET_ALIAS = "/api/ds";

    private static final long serialVersionUID = 1L;

    private HttpService httpService;

    public TestServletDS() {
    }

    @Activate
    protected void start() {

        try {

            httpService.registerServlet(SERVLET_ALIAS, this, null,
                    httpService.createDefaultHttpContext());
        } catch (ServletException | NamespaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();

        out.println("Request = " + req);
        out.println("PathInfo = " + req.getPathInfo());
        out.println("Big Hello From the Declarative Services (Annotation) Servlet");
    }

    @Reference(
            name = "http.service",
            service = HttpService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "unsetHttpService"
    )
    protected void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    protected void unsetHttpService(HttpService httpService) {
        this.httpService = null;
    }
}


