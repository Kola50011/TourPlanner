package at.fhtw.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("db")
@Data
@Validated
public class DBProperties {
    private String user;
    private String pass;
    private String url;
}
