/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package in.yagnyam.digid;

import com.google.api.server.spi.response.BadRequestException;

import in.yagnyam.myid.utils.PemUtils;
import in.yagnyam.myid.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterEndpointValidations {

    static void validateRegistrationRequest(RegistrationRequest request) throws BadRequestException {
        if (StringUtils.isEmpty(request.getVerificationKey())) {
            log.info("Verification Key is missing");
            throw new BadRequestException("Verification Key is missing");
        }
        try {
            PemUtils.decodePublicKey(request.getVerificationKey());
        } catch(Throwable t) {
            log.info("Invalid Verification Key " + request.getVerificationKey(), t);
            throw new BadRequestException("Invalid Verification Key");
        }
    }
}
