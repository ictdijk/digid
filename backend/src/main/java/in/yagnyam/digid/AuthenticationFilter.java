package in.yagnyam.digid;

import com.google.api.server.spi.response.UnauthorizedException;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.yagnyam.myid.utils.StringUtils;

public class AuthenticationFilter implements Filter {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private String realm = "DigiD";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String paramRealm = filterConfig.getInitParameter("realm");
        if (!StringUtils.isEmpty(paramRealm)) {
            realm = paramRealm;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.info("AuthenticationFilter.doFilter()");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();
                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        String credentials = new String(Base64.decode(st.nextToken()), "UTF-8");
                        logger.debug("Credentials: " + credentials);
                        int p = credentials.indexOf(":");
                        if (p != -1) {
                            String userName = credentials.substring(0, p).trim();
                            request.setAttribute("in.yagnyam.digana.userNumber", UUID.fromString(userName));
                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
                            unauthorized(response, "Invalid authentication token");
                        }
                    } catch (IllegalArgumentException | UnsupportedEncodingException e) {
                        unauthorized(response, "Invalid authentication token: " + e.getMessage());
                        return;
                    }
                }
            }
        } else {
            unauthorized(response);
        }
    }

    @Override
    public void destroy() {
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
        response.sendError(401, message);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        unauthorized(response, "Unauthorized");
    }


    public static String getUserName(HttpServletRequest req) throws UnauthorizedException {
        String authHeader = req.getHeader("authorization");
        if (StringUtils.isEmpty(authHeader)) {
            throw new UnauthorizedException("Not Authorized");
        }
        return getUserName(authHeader);
    }

    public static String getPassword(HttpServletRequest req) throws UnauthorizedException {
        String authHeader = req.getHeader("authorization");
        if (StringUtils.isEmpty(authHeader)) {
            throw new UnauthorizedException("Not Authorized");
        }
        return getPassword(authHeader);
    }


    public static String getUserName(String authHeader) throws UnauthorizedException {
        String[] authTokens = authHeader.split(" ");
        if (authTokens.length != 2 || !"Basic".equalsIgnoreCase(authTokens[0])) {
            logger.warn("Expecting Basic Authentication. Got: " + authHeader);
            throw new UnauthorizedException("invalid authentication header");
        }
        String[] loginCredentials = new String(Base64.decode(authTokens[1])).split(":");
        if (loginCredentials.length != 2) {
            logger.warn("Expecting Basic Authentication. Got: " + authHeader);
            throw new UnauthorizedException("invalid authentication header");
        }
        return loginCredentials[0];
    }


    public static String getPassword(String authHeader) throws UnauthorizedException {
        String[] authTokens = authHeader.split(" ");
        if (authTokens.length != 2 || !"Basic".equalsIgnoreCase(authTokens[0])) {
            logger.warn("Expecting Basic Authentication. Got: " + authHeader);
            throw new UnauthorizedException("invalid authentication header");
        }
        String[] loginCredentials = new String(Base64.decode(authTokens[1])).split(":");
        if (loginCredentials.length != 2) {
            logger.warn("Expecting Basic Authentication. Got: " + authHeader);
            throw new UnauthorizedException("invalid authentication header");
        }
        return loginCredentials[1];
    }


}