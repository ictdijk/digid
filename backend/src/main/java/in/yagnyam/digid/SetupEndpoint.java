package in.yagnyam.digid;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Api(
        name = "setupApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "digid.yagnyam.in",
                ownerName = "digid.yagnyam.in",
                packagePath = ""
        )
)
@Slf4j
public class SetupEndpoint {

    static {
        StaticRegistrar.register();
    }

    @ApiMethod(name = "setup", httpMethod = ApiMethod.HttpMethod.POST)
    public List<Person> setup(User user) throws UnauthorizedException {
        log.info("setup()");
        if (user == null || !user.getEmail().equalsIgnoreCase("chenna@alavala.in")) {
            throw new UnauthorizedException("Not Authorized to perform this action");
        }
        Person alice = createPerson("111111111", "Alie", new Date());
        Person bob = createPerson("222222222", "Bob", new Date());
        Person charlie = createPerson("333333333", "Charlie", new Date());
        Person dave = createPerson("444444444", "Dave", new Date());
        Person eve = createPerson("555555555", "Eve", new Date());
        ofy().save().entities(alice, bob, charlie, dave, eve).now();
        return Arrays.asList(alice, bob, charlie, dave, eve);
    }

    private Person createPerson(String bsn, String name, Date dob) {
        Person person = new Person();
        person.setDigid(bsn);
        person.setPassword(bsn);
        person.setBsn(bsn);
        person.setName(name);
        person.setDob(dob);
        return person;
    }


}
