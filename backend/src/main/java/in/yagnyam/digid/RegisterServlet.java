package in.yagnyam.digid;

import com.google.api.server.spi.ServiceException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.yagnyam.myid.utils.JsonUtils;
import in.yagnyam.myid.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Servlet to register DigiD user onto BlockChain
 */
@Slf4j
public class RegisterServlet extends HttpServlet {

    static {
        StaticRegistrar.register();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectTo = req.getParameter("requestURI");
        try {
            String digid = ServletUtils.getMandatoryParameter(req, "digid");
            String password = ServletUtils.getMandatoryParameter(req, "password");
            String verificationKey = ServletUtils.getMandatoryParameter(req, "verificationKey");
            Person person = RegisterEndpoint.fetchPerson(digid, password);
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setVerificationKey(verificationKey);
            RegistrationResponse response = RegisterEndpoint.registerPerson(person, registrationRequest);
            log.info("Registered {} with {}", person, registrationRequest);
            ServletUtils.setSuccessAttribute(req, "Registration Success!!");
            resp.getWriter().write(JsonUtils.toJson(response));
            resp.getWriter().flush();
            return;
        } catch (ServiceException e) {
            log.error("error while setting up user", e);
            if (StringUtils.isEmpty(redirectTo)) {
                throw new ServletException(e);
            }
            ServletUtils.setErrorAttribute(req, e.getMessage());
        }
        resp.sendRedirect(redirectTo);
    }

}
