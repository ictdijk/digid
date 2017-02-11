package in.yagnyam.digid;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import in.yagnyam.myid.utils.StringUtils;
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

    public static final String BSN = "bsn";
    public static final String NAME = "name";
    public static final String DOB = "dob";
    public static final String BLOOD_GROUP = "bloodGroup";
    public static final String REFUGEE_STATUS = "refugeeStatus";
    public static final String DEAF = "deaf";
    public static final String DUMB = "dumb";

    @Id
    private String digid;

    private String password;

    @Index
    private String bsn;

    private String name;

    private Date dob;

    private String bloodGroup;

    private String refugeeStatus;

    private String deaf;

    private String dumb;

    public Map<String, String> getClaims(List<String> onlyClaims) {
        Map<String, String> claims = new TreeMap<>();
        addToMap(claims, BSN, bsn, onlyClaims);
        addToMap(claims, NAME, name, onlyClaims);
        addToMap(claims, DOB, dob == null ? null : new SimpleDateFormat("yyyy-mm-dd", Locale.US).format(dob), onlyClaims);
        addToMap(claims, BLOOD_GROUP, bloodGroup, onlyClaims);
        addToMap(claims, REFUGEE_STATUS, refugeeStatus, onlyClaims);
        addToMap(claims, DEAF, deaf, onlyClaims);
        addToMap(claims, DUMB, dumb, onlyClaims);
        return claims;
    }

    private void addToMap(Map<String, String> map, String name, String value, List<String> onlyClaims) {
        if (onlyClaims != null && onlyClaims.contains(name) && !StringUtils.isEmpty(value)) {
            map.put(name, value);
        }
    }

}
