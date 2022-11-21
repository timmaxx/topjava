package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserRoles;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.validateFields;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final BeanPropertyRowMapper<UserRoles> ROW_MAPPER_USER_ROLES = BeanPropertyRowMapper.newInstance(UserRoles.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validateFields(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            batchInsert(newKey.intValue(), user);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users
                      SET name = :name,
                          email = :email,
                          password = :password,
                          registered = :registered,
                          enabled = :enabled,
                          calories_per_day = :caloriesPerDay
                    WHERE id = :id
                """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ?", user.getId());
            batchInsert(user.getId(), user);
        }
        return user;
    }

    private int[] batchInsert(int userId, User user) {
        if (user.getRoles() == null || user.getRoles().size() == 0) {
            return null;
        }
        List<Role> roles = user.getRoles().stream().toList();
        return this.jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setString(2, roles.get(i).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id);
        return getUserAndRoles(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email);
        return getUserAndRoles(users);
    }

    private User getUserAndRoles(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            return null;
        }
        user.setRoles(new ArrayList<>());
        List<UserRoles> userRoless = jdbcTemplate.query(
                "SELECT * FROM user_roles WHERE user_id = ? ORDER BY role",
                ROW_MAPPER_USER_ROLES, user.getId());
        for (int i = 0; i < userRoless.size(); i++) {
            int finalI = i;
            User loopUser = users.stream()
                    .filter(u -> u.getId().equals(userRoless.get(finalI).getUserId()))
                    .findAny()
                    .orElse(null);
            loopUser.getRoles().add(userRoless.get(i).getRole());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        for (User user : users) {
            user.setRoles(new ArrayList<>());
        }
        List<UserRoles> userRoless = jdbcTemplate.query(
                "SELECT * FROM user_roles ORDER BY role",
                ROW_MAPPER_USER_ROLES);
        for (int i = 0; i < userRoless.size(); i++) {
            int finalI = i;
            User user = users.stream()
                    .filter(u -> u.getId().equals(userRoless.get(finalI).getUserId()))
                    .findAny()
                    .orElse(null);
            user.getRoles().add(userRoless.get(i).getRole());
        }
        return users;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id) != 0;
    }
}
