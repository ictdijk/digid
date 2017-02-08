package in.yagnyam.digid;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "digid")
public class Person {

    @Id
    private String digid;

    private String password;

    @Index
    private String bsn;

    private String name;

    private Date dob;
}
