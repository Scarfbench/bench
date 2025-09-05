package org.apache.struts.examples.mailreader2.config;

import org.apache.struts.examples.mailreader2.dao.UserDatabase;
import org.apache.struts.examples.mailreader2.dao.impl.memory.MemoryUserDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${app.database.path}")
    private String dbPath;

    @Bean
    public UserDatabase userDatabase() {
        MemoryUserDatabase db = new MemoryUserDatabase();
        db.setPathname(dbPath);
        try {
            db.open();
        } catch (Exception e) {
            LOG.error("Opening memory database", e);
            throw new IllegalStateException("Cannot load database from '" +
                    dbPath + "': " + e);
        }

        return db;
    }

}
