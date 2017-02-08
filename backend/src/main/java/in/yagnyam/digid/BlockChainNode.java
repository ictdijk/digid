package in.yagnyam.digid;

import in.yagnyam.myid.utils.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BlockChainNode {

    private String path;

    private String description;

    private String dataHashSha256;

    private String dataHashMd5;

    // TODO: Only for development
    private String privateKey;

    private String verificationKey;

    private String signer;

    private String signatureSha256;

    private String signatureMd5;

    public String contentToSign() {
        return path
                + StringUtils.nonNull(description)
                + StringUtils.nonNull(dataHashSha256)
                + StringUtils.nonNull(dataHashMd5)
                + StringUtils.nonNull(verificationKey);
    }

}
