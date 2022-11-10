package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static ru.javawebinar.topjava.Profiles.*;


//@Profile({"!" + JPA, "!" + DATAJPA, HSQL_DB})
@Profile({HSQL_DB})
@Repository
public class JdbcMealHsqldbRepository extends JdbcMealRepository<Date> {

    public JdbcMealHsqldbRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Date prepareLocalDateTimeForDB(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
