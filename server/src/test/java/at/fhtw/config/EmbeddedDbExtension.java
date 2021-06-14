package at.fhtw.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@Slf4j
public class EmbeddedDbExtension implements BeforeAllCallback {

    @Override
    @BeforeAll
    public void beforeAll(ExtensionContext context) {
        // we store it in the root context, since that is closed after all tests are run
        ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);
        store.getOrComputeIfAbsent("db", foo -> startDb());
    }

    @SneakyThrows
    private ExtensionContext.Store.CloseableResource startDb() {
        final var db = EmbeddedPostgres.builder()
                .setPort(5432)
                .start();

        // DB will be closed automatically once the root context is closed due to this
        return new CloseableWrapper(db);
    }

    @RequiredArgsConstructor
    static class CloseableWrapper implements ExtensionContext.Store.CloseableResource {
        private final EmbeddedPostgres db;
        private boolean isClosed = false;

        @Override
        public void close() throws Throwable {
            if (!isClosed) {
                isClosed = true;
                db.close();
                log.info("db closed");
            }
        }
    }
}

