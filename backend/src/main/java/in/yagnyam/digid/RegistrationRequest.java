package in.yagnyam.digid;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class RegistrationRequest {

    private List<String> roles;

    private String verificationKey;
}