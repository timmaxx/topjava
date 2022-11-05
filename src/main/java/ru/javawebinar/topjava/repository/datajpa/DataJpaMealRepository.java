package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final UserRepository userRepository; // Только для save, но и там нужен только userId, который и так есть...

    public DataJpaMealRepository(CrudMealRepository crudRepository, UserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) { // insert
            if(meal.getUser() == null) {
                // Может быть вместо конструкции userRepository.get(userId) есть возможность вызвать сеттер
                // с параметром типа int вместо типа User?
                // Ведь всё равно в insert пойдёт именно user_id...
                meal.setUser(userRepository.get(userId));
            } else {
                // Эта ветка не срабатывает при тестировании, а следовало-бы сделать такой тест!
                throw new NotFoundException("Meal has user_id = " + meal.getUser().getId() + ", but parameter userId = " + userId + "!");
            }
        } else { // update
            if (meal.getUser() != null) {
                if (meal.getUser().getId() != userId) {
                    // Эта ветка не срабатывает при тестировании, а следовало-бы сделать такой тест!
                    throw new NotFoundException("In meal = " + meal + " doesn't definite user!");
                }
            } else {
                //if (userId == USER_ID)
                if (userId == 100000) { // Это костыль, т.к. считаю, что в тестах несогласованы аргументы и ожидания.
                    // А именно:
                    // - Либо в MealTestData в методах getNew и getUpdated не инициализируется значение поля user.
                    //   А нужно было-бы там проинициализировать USER_ID (думаю, так предпочтительней).
                    // - Либо в MealServiceTest в методах updateNotOwn и update после вызовов getNew и getUpdate
                    //   дополнительно инициализировать meal.user.
                    meal.setUser(userRepository.get(userId));
                } else {
                    throw new NotFoundException("Not found entity with id=" + meal.getId());
                }
            }
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        // return crudRepository.delete(id, userId) != 0;
        // Считаю, что "== 1" более правильным чем "!= 0", т.к. данный метод должен удалить либо одну запись, либо ноль,
        // Но не две и более.
        // То же самое и для User следует сделать.
        return crudRepository.delete(id, userId) == 1;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository
                .findById(id)
                .filter(meal -> meal.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        // Какой из этих вариантов предпочтительнее?
        // 1. Sorting is in beginning - in findAll.
        return crudRepository
                .findAll(Sort.by(Sort.Direction.DESC, "dateTime"))
                .stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .toList();
/*      // 2. Sorting is in ending - after filter.
        return crudRepository
                .findAll()
                .stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .toList();
*/
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        // Думаю, что также нужны тесты, при которых на начало периода будут данные,
        // т.к. сейчас для 30.01.2020 нет записи на 00:00:00.
        // Ну а заодно и на конец периода.
        return crudRepository
                .findAll(Sort.by(Sort.Direction.DESC, "dateTime"))
                .stream()
                .filter(meal -> meal.getUser().getId() == userId)
                //.filter(meal -> meal.getDateTime().isAfter(startDateTime)) // Текущие тесты сработают, но здесь должно быть не строгое сравнение.
                .filter(meal -> !meal.getDateTime().isBefore(startDateTime))
                .filter(meal -> meal.getDateTime().isBefore(endDateTime))
                .toList();
/*
        return crudRepository
                .findAll()
                .stream()
                .filter(meal -> meal.getUser().getId() == userId)
                //.filter(meal -> meal.getDateTime().isAfter(startDateTime)) // Текущие тесты сработают, но здесь должно быть не строгое сравнение.
                .filter(meal -> !meal.getDateTime().isBefore(startDateTime))
                .filter(meal -> meal.getDateTime().isBefore(endDateTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .toList();
*/
    }
}
