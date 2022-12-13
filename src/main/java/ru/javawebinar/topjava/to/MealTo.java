package ru.javawebinar.topjava.to;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;

// Этот класс нужен как отдельный DTO для передачи данных от сервера на браузер (клиент) для отображения (Select).
// В нём присутствует поле "excess", которое является вычисляемым агрегированным по группе записей еда.
// Есть отдельный класс для вставки и обновления MealToIU (Insert, Update) и именно для полей того класса установлены
// аннотации для валидации.
// В этом классе аннотации валидации удалены. Но в Meal оставлены для валидации перед сохранением (pre-persist).
public class MealTo extends BaseTo {

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return calories == mealTo.calories &&
                excess == mealTo.excess &&
                Objects.equals(id, mealTo.id) &&
                Objects.equals(dateTime, mealTo.dateTime) &&
                Objects.equals(description, mealTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
