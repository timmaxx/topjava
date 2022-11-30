package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users",
                        new AssertionMatcher<List<User>>() {
                            @Override
                            public void assertion(List<User> actual) throws AssertionError {
                                USER_MATCHER.assertMatch(actual, admin, guest, user);
                            }
                        }
                ));
    }


    // Вариант 1
    @Test
    void getMeals() throws Exception {
        perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<MealTo>>() {
                            @Override
                            public void assertion(List<MealTo> actual) throws AssertionError {
                                MEAL_TO_MATCHER.assertMatch(
                                        actual,
                                        MealsUtil.getTos(MealTestData.meals ,SecurityUtil.authUserCaloriesPerDay()));
                            }
                        }
                ));
    }

    /*
    // Вариант 2
    // 7: Попробуйте в RootControllerTest.getMeals сделать сравнение через model().attribute("meals", expectedValue).
    // Учтите, что вывод результатов через toString к сравнению отношения не имеет.

    // Не пойму, в чём подвох: посимвольно все элементы в выводе toString одинаковы, в toString для MealTo печатаются все поля...
    @Test
    void getMeals() throws Exception {
        perform(get("/meals"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("meals"))
            .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
            .andExpect(model().attribute(
                    "meals",
                    MealsUtil.getTos(MealTestData.meals ,SecurityUtil.authUserCaloriesPerDay())));
    }
    */


    // Пытался сузить до одной записи.
    // Здесь одна и та же единственная еда в каждом из списков. Что не так?
    @Test
    void getMeals100003() throws Exception {
        ResultActions resultActions = perform(get("/meals/filter?endDate=2020-01-30&endTime=11:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                ;

        resultActions.andExpect(model().attribute(
                        "meals",
                        MealsUtil.getTos(List.of(meal1) ,SecurityUtil.authUserCaloriesPerDay())));
    }
}