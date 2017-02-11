package in.yagnyam.digid;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class PersonAuthorization {

    @Id
    private String authorizationId;

    @Index
    private String digid;

    @Index
    private String path;

    private List<String> claims;

    private String verificationKey;
}
