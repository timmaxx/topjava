package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.to.MealToForIU;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static MealToForIU createToForIU(Meal meal) {
        return new MealToForIU(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    public static Meal createNewFromToForIU(MealToForIU mealToForIU) {
        return new Meal(null, mealToForIU.getDateTime(), mealToForIU.getDescription(), mealToForIU.getCalories());
    }

    public static Meal updateFromToForIU(Meal meal, MealToForIU mealToForIU) {
        meal.setDescription(mealToForIU.getDescription());
        meal.setDateTime(mealToForIU.getDateTime());
        meal.setCalories(mealToForIU.getCalories());
        return meal;
    }
}
