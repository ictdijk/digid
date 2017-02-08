package in.yagnyam.digid;

import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.response.BadRequestException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.yagnyam.myid.utils.PemUtils;
import in.yagnyam.myid.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Servlet to setup DigiD initial Users
 */
@Slf4j
public class SetupPersonsServlet extends HttpServlet {

    static {
        StaticRegistrar.register();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redirectTo = req.getParameter("requestURI");
        try {
            Person person = new Person();
            person.setDigid(ServletUtils.getMandatoryParameter(req, "digid"));
            person.setPassword(ServletUtils.getMandatoryParameter(req, "password"));
            person.setBsn(ServletUtils.getMandatoryParameter(req, "bsn"));
            person.setName(ServletUtils.getMandatoryParameter(req, "name"));
            person.setDob(stringToDate(ServletUtils.getMandatoryParameter(req, "dob")));
            ofy().save().entity(person).now();
            log.info("Saved {}", person);
            ServletUtils.setSuccessAttribute(req, "Saved Successfully");
        } catch (ServiceException e) {
            log.error("error while setting up user", e);
            if (StringUtils.isEmpty(redirectTo)) {
                throw new ServletException(e);
            }
            ServletUtils.setErrorAttribute(req, e.getMessage());
        }
        resp.sendRedirect(redirectTo);
    }

    public static List<Person> allPersons() {
        List<Person> persons = ofy().load().type(Person.class).list();
        log.info("Persons => {}", Arrays.toString(persons.toArray()));
        return persons;
    }

    private static Date stringToDate(String dateValue) throws BadRequestException {
        try {
            return new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault()).parse(dateValue);
        } catch (ParseException e) {
            throw new BadRequestException("Invalid Date: " + dateValue, e);
        }
    }

}
