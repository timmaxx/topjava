package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.to.MealToIU;
import ru.javawebinar.topjava.util.Util;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/profile/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {

    @Override
    @GetMapping("/{id}")
    // Для выборки одной записи используем Meal.
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    // Для выборки нескольких записей используем MealTo.
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // Для вставки или обновления одной записи используем MealToIU.
    public ResponseEntity<String> createOrUpdate(
            @Valid MealToIU mealToIU,
            BindingResult result) {
        if (result.hasErrors()) {
            return Util.buildResponseEntityFromBindingResult(result);
        }
/*
        try {
            // Если валидация по полям прошла успешно, то пробуем вставить или обновить.
            // Но!!!
            // Если в процессе вставки/обновления нарушится какое-либо ограничение БД
            // (например я попытался внести еду с датой/временем уже имеющейся у пользователя),
            // то на клиента не поступит текст этого исключения.
            // Т.е. на клиенте будет видно: "Ошибка 500". Что совсем не информативно!
            // В логе вполне вразумительно написано:
            // 16:21:15.883 ERROR org.hibernate.engine.jdbc.spi.SqlExceptionHelper.logExceptions:142 - ОШИБКА: повторяющееся значение ключа нарушает ограничение уникальности "meals_unique_user_datetime_idx"
            //  Detail: Ключ "(user_id, date_time)=(100000, 2020-01-31 00:00:00)" уже существует.
            if (mealToIU.isNew()) {
                super.create(mealToIU);
            } else {
                super.update(mealToIU, mealToIU.id());
            }
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            // Исключения, которые попробовал ловить:
            // org.hibernate.exception.ConstraintViolationException --
            // javax.validation.ConstraintViolationException --
            // org.springframework.dao.DataIntegrityViolationException ++
            // NestedServletException --
            // InvocationTargetException --
            // System.out.println("1 " + dive);
            // System.out.println("2 " + dive.getCause());
            // System.out.println("3 " + dive.getRootCause());
            // System.out.println("4 " + dive.getMostSpecificCause());
            return ResponseEntity.unprocessableEntity().body(dive.getMostSpecificCause().toString());
        }
*/
        // Пока вариант без обработки исключений, возникших из-за ошибок целостности, произошедших на сервере.
        if (mealToIU.isNew()) {
            super.create(mealToIU);
        } else {
            super.update(mealToIU, mealToIU.id());
        }

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/filter")
    // Для выборки нескольких записей используем MealTo.
    public List<MealTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}