package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Controller
public class MealRestController extends AbstractMealController {

    @Override
    public Meal create(Meal meal) {
        return super.create(meal);
    }

    @Override
    public Meal get(int id) {
        return super.get(id);
    }

    @Override
    public List<MealTo> getAll() {
        return super.getAll();
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    @Override
    public List<MealTo> getBetween(LocalDate startDate, LocalTime startTime,
                                   LocalDate endDate, LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    public void update(Meal meal, int id) {
        super.update(meal, id);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }
}
