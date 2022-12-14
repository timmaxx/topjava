package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.to.MealToCreateUpdate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// На начало HW09 класс имел имя MealsUtil. Это не единообразно с UserUtil. Переименовал.
public class MealUtil {

    private MealUtil() {
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> Util.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .toList();
    }

    // В UserUtil подобный метод имеет имя asTo:
    // public static UserTo asTo(User user)
    // ToDo: лучше сделать единообразно.
    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static Meal createNewFromToCreateUpdate(MealToCreateUpdate mealToCreateUpdate) {
        return new Meal(mealToCreateUpdate.getId(), mealToCreateUpdate.getDateTime(), mealToCreateUpdate.getDescription(), mealToCreateUpdate.getCalories());
    }

    // Используется только в MealRestControllerTest::update()
    // Может тогда его вынести в отдельный утилитный класс в ветку тестов?
    public static MealToCreateUpdate createToCreateUpdate(Meal meal) {
        return new MealToCreateUpdate(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    // Используется только в MealRestControllerTest::update()
    // Может тогда его вынести в отдельный утилитный класс в ветку тестов?
    public static Meal updateFromToCreateUpdate(Meal meal, MealToCreateUpdate mealToCreateUpdate) {
        meal.setDescription(mealToCreateUpdate.getDescription());
        meal.setDateTime(mealToCreateUpdate.getDateTime());
        meal.setCalories(mealToCreateUpdate.getCalories());
        return meal;
    }
}