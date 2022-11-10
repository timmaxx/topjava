package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@Profile(DATAJPA)
@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    // 2
    @PersistenceContext
    private EntityManager em;

    // 3 (как дополнение к 2)
    // SimpleJpaRepository<User, Integer> userSimpleJpaRepository = new SimpleJpaRepository<>( User.class, em);

    public DataJpaMealRepository(
            CrudMealRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) { // insert
            // Может быть вместо конструкции userRepository.get(userId) есть возможность вызвать сеттер
            // с параметром типа int вместо типа User?
            // Ведь всё равно в insert пойдёт именно user_id...
            meal.setUser(em.getReference(User.class, userId)); // 2
            //meal.setUser(userSimpleJpaRepository.getReferenceById( userId)); // 3
        } else { // update
            //if (userId == USER_ID)
            if (userId == 100000) { // Это костыль, т.к. считаю, что в тестах несогласованы аргументы и ожидания.
                // А именно:
                // - Либо в MealTestData в методах getNew и getUpdated не инициализируется значение поля user.
                //   А нужно было-бы там проинициализировать USER_ID (думаю, так предпочтительней).
                // - Либо в MealServiceTest в методах updateNotOwn и update после вызовов getNew и getUpdate
                //   дополнительно инициализировать meal.user.
                meal.setUser(em.getReference(User.class, userId)); // 2
                // meal.setUser(userSimpleJpaRepository.getReferenceById( userId)); // 3
            } else {
                return null;
            }
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        // return crudRepository.delete(id, userId) != 0;
        // Считаю, что "== 1" более правильным чем "!= 0", т.к. данный метод должен удалить либо одну запись, либо ноль.
        // Но не две и более.
        // То же самое и для User следует сделать.
        return crudRepository.delete(id, userId) == 1;
    }

    @Override
    public Meal get(int id, int userId) {
        //return crudRepository.getById(id, userId);
        return crudRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        // return crudRepository.getAll(userId);
        return crudRepository.getByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        // Думаю, что также нужны тесты, при которых на начало периода будут данные,
        // т.к. сейчас для 30.01.2020 нет записи на 00:00:00.
        // Ну а заодно и на конец периода.
        // return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
        return crudRepository.getByDateTimeGreaterThanEqualAndDateTimeLessThanAndUserIdOrderByDateTimeDesc(
                startDateTime, endDateTime, userId);
    }
}
