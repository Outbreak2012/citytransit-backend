package com.citytransit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ClickHouseConfig {

    private final DataSource dataSource;

    public ClickHouseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate clickHouseJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
