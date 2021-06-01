package at.fhtw.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties("minio")
@Data
@Validated
public class MinioProperties {
    @NotNull
    private String bucket;
    @NotNull
    private String endpoint;
    @NotNull
    private String accesskey;
    @NotNull
    private String secretKey;
}
