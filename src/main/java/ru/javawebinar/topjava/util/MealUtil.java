package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.to.MealToIU;

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

    public static MealToIU createToIU(Meal meal) {
        return new MealToIU(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    public static Meal createNewFromToIU(MealToIU mealToIU) {
        return new Meal(mealToIU.getId(), mealToIU.getDateTime(), mealToIU.getDescription(), mealToIU.getCalories());
    }

    public static Meal updateFromToIU(Meal meal, MealToIU mealToIU) {
        meal.setDescription(mealToIU.getDescription());
        meal.setDateTime(mealToIU.getDateTime());
        meal.setCalories(mealToIU.getCalories());
        return meal;
    }
}