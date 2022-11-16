package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;


    @GetMapping("")
    public String getMeals(
            Model model,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "id", required = false) Integer mealId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        log.info("meals");

        switch (action == null ? "all" : action) {
            case "delete" -> {
                service.delete( mealId, SecurityUtil.authUserId());
                model.addAttribute("meals",
                        MealsUtil.getTos(
                                service.getAll(SecurityUtil.authUserId()),
                                SecurityUtil.authUserId()));
                return "meals";
            }
            case "create" -> {
                model.addAttribute( "meal", new Meal());
                return "mealForm";
            }
            case "update" -> {
                model.addAttribute( "meal", service.get( mealId, SecurityUtil.authUserId()));
                return "mealForm";
            }
            case "filter" -> {
                model.addAttribute("meals",
                        MealsUtil.getFilteredTos(
                                service.getBetweenInclusive(
                                        parseLocalDate(startDate),
                                        parseLocalDate(endDate),
                                        SecurityUtil.authUserId()),
                                SecurityUtil.authUserId(),
                                parseLocalTime(startTime),
                                parseLocalTime(endTime)));
                return "meals";
            }
            default -> {
                model.addAttribute("meals",
                        MealsUtil.getTos(
                                service.getAll(SecurityUtil.authUserId()),
                                SecurityUtil.authUserId()));
                return "meals";
            }
        }
    }


    @PostMapping("")
    public String setMeal(HttpServletRequest request) {
        log.info("setMeal {}");

        Meal meal;
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        Integer calories = Integer.parseInt(request.getParameter("calories"));
        if (request.getParameter("id") == null || request.getParameter("id").equals("")) {
            meal = new Meal(
                    dateTime, description, calories);
            service.create(meal, SecurityUtil.authUserId());
        } else {
            meal = new Meal(
                    Integer.parseInt(request.getParameter("id")),
                    dateTime, description, calories);
            service.update(meal, SecurityUtil.authUserId());
        }
        return "redirect:meals";
    }
}
