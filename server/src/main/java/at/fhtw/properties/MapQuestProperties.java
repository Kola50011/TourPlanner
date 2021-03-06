package at.fhtw.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties("map-quest")
@Data
@Validated
public class MapQuestProperties {
    @NotNull
    private String secretKey;
    @NotNull
    private String endpoint;
}
