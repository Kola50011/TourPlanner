package at.fhtw.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties("db")
@Data
@Validated
public class DBProperties {
    @NotNull
    private String user;
    @NotNull
    private String pass;
    @NotNull
    private String url;
}
