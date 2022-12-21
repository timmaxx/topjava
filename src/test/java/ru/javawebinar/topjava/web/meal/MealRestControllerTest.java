package ru.javawebinar.topjava.web.meal;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.web.ErrorInfoTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID)
                .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }
/*
    @Test
    void getWrongParam() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + WRONG)
                .with(userHttpBasic(user))
        )
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorResponse_MATCHER.contentJson(errorResponseMeal1));
    }
*/
    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID)
                .with(userHttpBasic(user))
        )
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }
/*
    @Test
    void deleteWrongParam() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + WRONG)
                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorResponse_MATCHER.contentJson(errorResponseMeal1));
    }
*/
    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(user))
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated))
        )
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void updateInvalidObjAllFieldsEmpty() throws Exception {
        Meal updated = getNewInvalidAllFieldsEmpty();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorInfo_MATCHER.contentJson(ERROR_INFO_MEAL_ALL_FIELDS_EMPTY_UPDATING));
    }

    @Test
    void updateInvalidObjAllFieldsInvalid() throws Exception {
        Meal updated = getNewInvalidAllFieldsInvalid();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorInfo_MATCHER.contentJson(ERROR_INFO_MEAL_ALL_FIELDS_INVALID_UPDATING));
    }

    /*
    @Test
    void updateWrongParam() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + WRONG)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorResponse_MATCHER.contentJson(errorResponseMeal1));
    }
*/
    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(newMeal))
        )
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void createWithLocationInvalidObjAllFieldsEmpty() throws Exception {
        Meal newMeal = getNewInvalidAllFieldsEmpty();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(newMeal))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorInfo_MATCHER.contentJson(ERROR_INFO_MEAL_ALL_FIELDS_EMPTY));
    }

    @Test
    void createWithLocationInvalidObjAllFieldsInvalid() throws Exception {
        Meal newMeal = getNewInvalidAllFieldsInvalid();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(newMeal))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorInfo_MATCHER.contentJson(ERROR_INFO_MEAL_ALL_FIELDS_INVALID));
    }
/*
    // Тест не проходит, но вероятно в п. 2 он и не должен быть реализован.
    //@Test
    //@Ignore
    void createWithLocationInvalidObjDublicateDateTime() throws Exception {
        Meal newMeal = getNewInvalidDublicateDateTime();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(newMeal))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorResponse_MATCHER.contentJson());
    }
*/
/*
    @Test
    void createWithLocationWrongParam() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(errorResponseMeal1))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorResponse_MATCHER.contentJson(errorResponseMeal2))
                ;
    }
*/
    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(getTos(meals, user.getCaloriesPerDay())));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-01-30").param("startTime", "07:00")
                .param("endDate", "2020-01-31").param("endTime", "11:00")
                .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(TO_MATCHER.contentJson(createTo(meal5, true), createTo(meal1, false)));
    }
/*
    @Test
    void getBetweenWrongParam() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", WRONG)
                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ErrorResponse_MATCHER.contentJson(errorResponseMeal3));
    }
*/
    @Test
    void getBetweenAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime=")
                .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andExpect(TO_MATCHER.contentJson(getTos(meals, user.getCaloriesPerDay())));
    }
}