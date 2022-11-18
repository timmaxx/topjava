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
    public String getAll(Model model) {
        log.info("getAll");
        model.addAttribute("meals",
                MealsUtil.getTos(
                        service.getAll(SecurityUtil.authUserId()),
                        SecurityUtil.authUserId()));
        return "/meals";
    }

    @GetMapping("/filter")
    public String getByFilter(
            Model model,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        log.info("getByFilter");
        model.addAttribute("meals",
                MealsUtil.getFilteredTos(
                        service.getBetweenInclusive(
                                parseLocalDate(startDate),
                                parseLocalDate(endDate),
                                SecurityUtil.authUserId()),
                        SecurityUtil.authUserId(),
                        parseLocalTime(startTime),
                        parseLocalTime(endTime)));
        return "/meals";
        //return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("create");
        model.addAttribute( "meal", new Meal());
        return "mealForm";
    }

    @GetMapping("/update/{id}")
    public String update(
            Model model,
            @PathVariable( "id") Integer id) {
        log.info("update");
        model.addAttribute( "meal", service.get(id, SecurityUtil.authUserId()));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            Model model,
            @PathVariable( "id") Integer id) {
        log.info("delete");
        service.delete(id, SecurityUtil.authUserId());
        model.addAttribute("meals",
                MealsUtil.getTos(
                        service.getAll(SecurityUtil.authUserId()),
                        SecurityUtil.authUserId()));
        return "redirect:/meals";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request) {
        log.info("(post)create");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        Integer calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(
                dateTime, description, calories);
        service.create(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @PostMapping("/update/{id}")
    public String update(HttpServletRequest request) {
        log.info("(post)update");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        Integer calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(
                Integer.parseInt(request.getParameter("id")),
                dateTime, description, calories);
        service.update(meal, SecurityUtil.authUserId());
        return "redirect:/meals";
    }
}
