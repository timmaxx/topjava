package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.AbstractMealController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "/meals";
    }

    @GetMapping("/filter")
    public String getBetween(
            Model model,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        model.addAttribute(
                "meals",
                super.getBetween(
                        parseLocalDate(startDate), parseLocalTime(startTime),
                        parseLocalDate(endDate), parseLocalTime(endTime)));
        return "/meals";
        //return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(
            Model model,
            @PathVariable( "id") Integer id) {
        model.addAttribute( "meal", super.get(id));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            Model model,
            @PathVariable( "id") Integer id) {
        super.delete(id);
        model.addAttribute("meals", super.getAll());
        return "redirect:/meals";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request) {
        Meal meal = createMealFromRequest(request, null);
        super.create(meal);
        return "redirect:/meals";
    }

    @PostMapping("/update/{id}")
    public String update(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Meal meal = createMealFromRequest(request, id);
        super.update(meal, id);
        return "redirect:/meals";
    }

    private Meal createMealFromRequest(HttpServletRequest request, Integer id) {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(id, dateTime, description, calories);
    }
}
