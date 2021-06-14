package at.fhtw;

import at.fhtw.config.EmbeddedDbExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({
        EmbeddedDbExtension.class
})
@SpringBootTest
@ActiveProfiles("test")
class AppTest {
    @Autowired
    ApplicationContext context;

    @Test
    void contextStarts() {
        assertThat(context).isNotNull();
    }
}
