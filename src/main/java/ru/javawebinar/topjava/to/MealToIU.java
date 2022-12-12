package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;

// Этот класс нужен как отдельный DTO для приёма сервером от браузера для вставки (Insert) или обновления (Update).
// В нём отсутствует поле "excess", которое было вычисляемым агрегированным по группе записей еда и нужно было для отображения на клиенте.
public class MealToIU extends BaseTo {
// Если именно для полей этого класса установлены аннотации для валидации, то нужны-ли такие аннотации в Meal и в MealTo?
    @NotNull
    private final LocalDateTime dateTime;

    @NotBlank
    @Size(min = 2, max = 120)
    private final String description;

    @NotNull
    @Range(min = 10, max = 10000)
    // Если тип сделать int, и если в окне ввода/редактировании еды поле оставить пустым будет исключение:
    // convert value of type 'String' to required type 'int'.
    // Т.е. до @NotNull не доходило дело, и для пользователя в т.ч. был менее информативен текст ошибки.
    private final Integer calories;

    @ConstructorProperties({"id", "dateTime", "description", "calories"})
    public MealToIU(Integer id, LocalDateTime dateTime, String description, Integer calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCalories() {
        return calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealToIU mealToIU = (MealToIU) o;
        return  // calories == mealToIU.calories &&
                Objects.equals(calories, mealToIU.calories) &&
                Objects.equals(id, mealToIU.id) &&
                Objects.equals(dateTime, mealToIU.dateTime) &&
                Objects.equals(description, mealToIU.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories);
    }

    @Override
    public String toString() {
        return "MealToForIU{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}