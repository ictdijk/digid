/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package in.yagnyam.digid;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.Key;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.PriorityQueue;
import java.util.UUID;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import in.yagnyam.myid.utils.JsonUtils;
import in.yagnyam.myid.utils.PemUtils;
import in.yagnyam.myid.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static in.yagnyam.digid.RegisterEndpointValidations.validateRegistrationRequest;

@Api(
        name = "registerApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "digid.yagnyam.in",
                ownerName = "digid.yagnyam.in",
                packagePath = ""
        )
)
@Slf4j
public class RegisterEndpoint {


    @ApiMethod(name = "me")
    public Person me(HttpServletRequest httpRequest) throws UnauthorizedException {
        log.info("me()");
        Person person = fetchPerson(httpRequest);
        log.info("me => {}", person);
        return person;
    }

    @ApiMethod(name = "register", httpMethod = ApiMethod.HttpMethod.POST)
    public RegistrationResponse register(HttpServletRequest httpRequest, RegistrationRequest request) throws UnauthorizedException, BadRequestException, InternalServerErrorException {
        log.info("register(" + request + ")");
        Person person = fetchPerson(httpRequest);
        validateRegistrationRequest(request);
        return registerPerson(person, request);
    }

    public static RegistrationResponse registerPerson(Person person, RegistrationRequest request) throws InternalServerErrorException {
        PersonAuthorization authorization = createAuthorization(person, request);
        postToBlockChain(createBlockChainNode(person, authorization));
        return createRegistrationResponse(person, authorization);
    }

    private static Person fetchPerson(HttpServletRequest httpRequest) throws UnauthorizedException {
        String digid = AuthenticationFilter.getUserName(httpRequest);
        String password = AuthenticationFilter.getPassword(httpRequest);
        return fetchPerson(digid, password);
    }


    public static Person fetchPerson(String digid, String password) throws UnauthorizedException {
        Person person = ofy().load().key(Key.create(Person.class, digid)).now();
        if (person == null) {
            log.info("Invalid DigiD: {}", digid);
            throw new UnauthorizedException("Invalid DigiD or Password");
        } else if (!person.getPassword().equals(password)) {
            log.info("Invalid Password {} for DigiD {}", password, digid);
            throw new UnauthorizedException("Invalid DigiD or Password");
        }
        return person;
    }

    private static PersonAuthorization createAuthorization(Person person, RegistrationRequest request) {
        PersonAuthorization authorization = new PersonAuthorization();
        authorization.setAuthorizationId(UUID.randomUUID().toString());
        authorization.setPath("/person/" + authorization.getAuthorizationId());
        authorization.setDigid(person.getDigid());
        authorization.setRoles(request.getRoles());
        authorization.setVerificationKey(request.getVerificationKey());
        ofy().save().entity(authorization).now();
        return authorization;
    }

    // TODO: Handle all fields
    private static BlockChainNode createBlockChainNode(Person person, PersonAuthorization authorization) throws InternalServerErrorException {
        try {
            SigningKey key = fetchSigningKey();
            PrivateKey privateKey = PemUtils.decodePrivateKey(key.getPrivateKey());
            BlockChainNode blockChainNode = new BlockChainNode();
            blockChainNode.setPath(authorization.getPath());
            blockChainNode.setVerificationKey(authorization.getVerificationKey());
            blockChainNode.setSigner("/root/" + key.getName());
            blockChainNode.setDescription("Node for BSN: " + person.getBsn() + ". Hash is for BSN");
            blockChainNode.setDataHashMd5(SignUtils.getHash(person.getBsn(), SignUtils.ALGORITHM_MD5));
            blockChainNode.setDataHashSha256(SignUtils.getHash(person.getBsn(), SignUtils.ALGORITHM_SHA256));
            String signData = blockChainNode.contentToSign();
            blockChainNode.setSignatureMd5(SignUtils.getSignature(signData, SignUtils.ALGORITHM_MD5WithRSA, privateKey));
            blockChainNode.setSignatureSha256(SignUtils.getSignature(signData, SignUtils.ALGORITHM_SHA256WithRSA, privateKey));
            return blockChainNode;
        } catch (GeneralSecurityException | IOException e) {
            log.error("Failed to Sign " + person + " with authorization " + authorization, e);
            throw new InternalServerErrorException("Failed to create Node for Block Chain");
        }
    }

    private static RegistrationResponse createRegistrationResponse(Person person, PersonAuthorization authorization) {
        RegistrationResponse response = new RegistrationResponse();
        response.setBsn(person.getBsn());
        response.setName(person.getName());
        response.setDob(person.getDob());
        response.setDigid(person.getDigid());
        response.setPath(authorization.getPath());
        response.setRoles(authorization.getRoles());
        return response;
    }

    private static void postToBlockChain(BlockChainNode blockChainNode) throws InternalServerErrorException {
        try {
            URL url = new URL("" + blockChainNode.getPath());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(URLEncoder.encode(JsonUtils.toJson(blockChainNode), "UTF-8"));
            writer.close();
            int respCode = conn.getResponseCode();
            if (respCode != HttpURLConnection.HTTP_OK) {
                log.warn("Request Status: " + respCode + ", Message:" + conn.getResponseMessage());
                throw new InternalServerErrorException("Failed to post to BlockChain");
            }
        } catch (IOException e) {
            log.warn("Failed to Post to BlockChain", e);
            throw new InternalServerErrorException("Failed to Post to BlockChain", e);
        }
    }

    private static SigningKey fetchSigningKey() throws InternalServerErrorException {
        SigningKey key = ofy().load().type(SigningKey.class).first().now();
        if (key == null) {
            log.error("Key configuration is missing");
            throw new InternalServerErrorException("Missing Configuration");
        }
        log.info("Signing Key => {}", key);
        return key;
    }

}
