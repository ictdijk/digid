package in.yagnyam.digid;

import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.response.BadRequestException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.yagnyam.myid.utils.PemUtils;
import in.yagnyam.myid.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Servlet to setup DigiD initial SigningKey
 */
@Slf4j
public class SetupKeysServlet extends HttpServlet {

    static {
        StaticRegistrar.register();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectTo = req.getParameter("requestURI");
        try {
            String name = ServletUtils.getMandatoryParameter(req, "name");
            String privateKey = ServletUtils.getMandatoryParameter(req, "signingKey");
            validatePrivateKey(privateKey);
            SigningKey signingKey = new SigningKey();
            signingKey.setName(name);
            signingKey.setPrivateKey(privateKey);
            ofy().save().entity(signingKey).now();
            log.info("Saved {}", signingKey);
            ServletUtils.setSuccessAttribute(req, "Saved Successfully");
        } catch (ServiceException e) {
            log.error("error while setting up", e);
            if (StringUtils.isEmpty(redirectTo)) {
                throw new ServletException(e);
            }
            ServletUtils.setErrorAttribute(req, e.getMessage());
        }
        resp.sendRedirect(redirectTo);
    }

    private static void validatePrivateKey(String privateKey) throws BadRequestException {
        try {
            PemUtils.decodePrivateKey(privateKey);
        } catch (GeneralSecurityException | IOException e) {
            log.warn("Invalid Private Key", e);
            throw new BadRequestException("Invalid Private Key");
        }
    }

    public static List<SigningKey> allSigningKeys() {
        List<SigningKey> keys = ofy().load().type(SigningKey.class).list();
        log.info("Signing Keys => {}", Arrays.toString(keys.toArray()));
        return keys;
    }

}
