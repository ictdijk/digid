package in.yagnyam.digid;

import org.jose4j.base64url.Base64;

import java.util.Map;
import java.util.TreeMap;

import in.yagnyam.myid.utils.AsnSerializer;
import in.yagnyam.myid.utils.SignUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BlockChainNode {

    private String path;

    private String description;

    //private String dataHashSha256;

    //private String dataHashMd5;

    private Map<String, String> claims;

    //private String claimsHashSha256;

    //private String claimsHashMd5;

    // TODO: Only for development
    private String privateKey;

    private String verificationKey;

    private String signer;

    private String signatureSha256;

    private String signatureMd5;

    @SneakyThrows
    public String getClaimsEncoded() {
        AsnSerializer serializer = new AsnSerializer();
        if (claims != null) {
            TreeMap<String, String> sortedMap = new TreeMap<>(claims);
            for (Map.Entry<String, String> e : sortedMap.entrySet()) {
                serializer.addString(e.getKey());
                serializer.addString(e.getValue());
            }
        }
        serializer.addString(verificationKey);
        return Base64.encode(serializer.getEncoded());
    }

    public String getClaimsHashSha256() {
        return SignUtils.getHash(getClaimsEncoded(), SignUtils.ALGORITHM_SHA256);
    }

    public String getClaimsHashMd5() {
        return SignUtils.getHash(getClaimsEncoded(), SignUtils.ALGORITHM_MD5);
    }

    @SneakyThrows
    public String contentToSign() {
        AsnSerializer serializer = new AsnSerializer();
        serializer.addString(description);
        serializer.addString(getClaimsHashSha256());
        serializer.addString(getClaimsHashMd5());
        serializer.addString(verificationKey);
        return Base64.encode(serializer.getEncoded());
        /*
        return path
                + StringUtils.nonNull(description)
                + StringUtils.nonNull(dataHashSha256)
                + StringUtils.nonNull(dataHashMd5)
                + StringUtils.nonNull(verificationKey);
                */
    }

}
