package in.yagnyam.digid;

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

    private String verificationKey;

    private String signer;

    private String signatureSha256;

    private String signatureMd5;

}
