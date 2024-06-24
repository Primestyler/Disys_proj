package StationDataCollector.StationDataCollector.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceConfig {

    public DataSource createDataSource(String url) {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://" + url)
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
