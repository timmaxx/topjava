package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;


@Profile("hsqldb")
@Repository
public class JdbcMealHSQLDBRepository extends JdbcMealRepository<java.util.Date> {
    private final static int TIME_ZONE_OFFSET = 10;

    public JdbcMealHSQLDBRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Date localDateTime_to_Date_or_LocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.ofHours(TIME_ZONE_OFFSET)));
    }
}
