package ru.javawebinar.topjava.web.meal;

//import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/meals";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }


    // Для задания 3.
    // Наверное должно быть так.
    // Но нужно как-то классы конвертеры заинжектить. (Думал в spring-app конвертеры добавить, но в тестах не сработало).
    // Не работает.
    @Override
    @GetMapping("/filter")
    public List<MealTo> getBetween(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) LocalTime endTime) {
        System.out.println("List<MealTo> getBetween()");
        System.out.println("startDate = " + startDate);
        System.out.println("startTime = " + startTime);
        System.out.println("endDate = " + endDate);
        System.out.println("endTime = " + endTime);
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

/*  // Вариант конвертирования с явным вызовом конвертации.
    // Работает, но:
    // - не Override
    // - Если в коде будет вызов с null параметрами getBetween(null, null, null, null) (например в SpringMain),
    //   то компилятор не сможет выбрать из этого метода и родительского.
    @GetMapping("/filter")
    public List<MealTo> getBetween(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String endTime) {
        StringToLocalDate stringToLocalDate = new StringToLocalDate();
        StringToLocalTime stringToLocalTime = new StringToLocalTime();

        LocalDate ldStartDate = stringToLocalDate.convert(startDate);
        LocalTime ltStartTime = stringToLocalTime.convert(startTime);
        LocalDate ldEndDate = stringToLocalDate.convert(endDate);
        LocalTime ltEndTime = stringToLocalTime.convert(endTime);

        return super.getBetween(ldStartDate, ltStartTime, ldEndDate, ltEndTime);
    }
*/
/*  // Вариант с конвертированием с ипользованием аннотации форматирования @DateTimeFormat
    // Работает.
    @Override
    @GetMapping("/filter")
    public List<MealTo> getBetween(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            @RequestParam(required = false) LocalTime startTime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) LocalDate endDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            @RequestParam(required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
*/

    // Для задания 2.2.
    @Deprecated
    @GetMapping("/filter22")
    public List<MealTo> getBetween22(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false) LocalDateTime startDateTime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false) LocalDateTime endDateTime) {
        return super.getBetween(
                startDateTime.toLocalDate(),
                startDateTime.toLocalTime(),
                endDateTime.toLocalDate(),
                endDateTime.toLocalTime());
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}