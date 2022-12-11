package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealToForIU extends BaseTo {

    @NotNull
    private final LocalDateTime dateTime;

    @NotBlank
    @Size(min = 2, max = 120)
    private final String description;

    @NotNull
    @Range(min = 10, max = 10000)
    private final int calories;

    @ConstructorProperties({"id", "dateTime", "description", "calories"})
    public MealToForIU(Integer id, LocalDateTime dateTime, String description, int calories) {
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

    public int getCalories() {
        return calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealToForIU mealToForIU = (MealToForIU) o;
        return calories == mealToForIU.calories &&
                Objects.equals(id, mealToForIU.id) &&
                Objects.equals(dateTime, mealToForIU.dateTime) &&
                Objects.equals(description, mealToForIU.description);
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