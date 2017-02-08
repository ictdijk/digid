package in.yagnyam.digid;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class RegistrationResponse {

    private String digid;

    private String name;

    private String bsn;

    private Date dob;

    private String path;

    private List<String> roles;
}