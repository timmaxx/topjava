package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import ru.javawebinar.topjava.repository.JpaUtil;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    protected JpaUtil jpaUtil;

    @Before
    public void setup() {
        if (isJdbcProfileSet()) {
            return;
        }
        cacheManager.getCache("users").clear();
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    public void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void createForUserWith2Roles() {
        User created = service.create(getNewForUserWith2Roles());
        int newId = created.id();
        User newUser = getNewForUserWith2Roles();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    public void deleteForUserWith2Roles() {
        service.delete(ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getForUserWith2Roles() {
        User user = service.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("user@yandex.ru");
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getByEmailForUserWith2Roles() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), getUpdated());
    }

    @Test
    public void updateForUserWith2Roles() {
        User updated = getUpdatedForUserWith2Roles();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), getUpdatedForUserWith2Roles());
    }

    @Test
    public void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }

/*
    // Из-за кэширования в тестах может возникнуть ошибка.
    // Для её демонстрации нужно:
    // 1. Для этого класса должно быть
    //    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    // 2. Для JdbcUserServiceTest закомментировать
    //    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    // 3. раскомментировать эти два метода
    @Test
    public void a_update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), getUpdated());
        List<User> all = service.getAll();
    }

    @Test
    public void b_getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }
*/

    @Test
    public void createWithException() throws Exception {
        // Поскольку валидация сделана и для jdbc, то этот тест тоже должен проходить.
        // if (isJdbcProfileSet()) { return; }
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }
}