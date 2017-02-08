package in.yagnyam.digid;


import com.google.api.server.spi.response.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import in.yagnyam.myid.utils.StringUtils;

public class ServletUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);
    public static final String SUCCESS_ATTRIBUTE = "success";
    public static final String ERROR_ATTRIBUTE = "error";

    public static String getMandatoryParameter(HttpServletRequest request, String name) throws BadRequestException {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value)) {
            logger.debug("{} not set", name);
            throw new BadRequestException(name + " not set");
        }
        return value.trim();
    }


    public static double getMandatoryDoubleParameter(HttpServletRequest request, String name) throws BadRequestException {
        try {
            return Double.valueOf(getMandatoryParameter(request, name));
        } catch (NumberFormatException e) {
            throw new BadRequestException(e);
        }
    }



    public static void removeStatusAttributes(HttpServletRequest req) {
        removeAttribute(req, SUCCESS_ATTRIBUTE);
        removeAttribute(req, ERROR_ATTRIBUTE);
    }

    public static void setErrorAttribute(HttpServletRequest req, Object value) {
        removeAttribute(req, SUCCESS_ATTRIBUTE);
        setAttribute(req, ERROR_ATTRIBUTE, value);
    }

    public static void setSuccessAttribute(HttpServletRequest req, Object value) {
        removeAttribute(req, ERROR_ATTRIBUTE);
        setAttribute(req, SUCCESS_ATTRIBUTE, value);
    }

    public static Object getErrorAttribute(HttpServletRequest req) {
        return getAttribute(req, ERROR_ATTRIBUTE);
    }

    public static Object getSuccessAttribute(HttpServletRequest req) {
        return getAttribute(req, SUCCESS_ATTRIBUTE);
    }


    public static Object getAttribute(HttpServletRequest req, String name) {
        return req.getSession(true).getAttribute(name);
    }

    public static void setAttribute(HttpServletRequest req, String name, Object value) {
        req.getSession(true).setAttribute(name, value);
    }

    public static void removeAttribute(HttpServletRequest req, String name) {
        req.getSession(true).removeAttribute(name);
    }

    public static void consumeStatusAttributes(HttpServletRequest request, PageContext pageContext) {
        pageContext.setAttribute(SUCCESS_ATTRIBUTE, getSuccessAttribute(request));
        pageContext.setAttribute(ERROR_ATTRIBUTE, getErrorAttribute(request));
        removeStatusAttributes(request);
    }

}
