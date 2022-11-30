package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MealTo {

    // Представлены варианты для разрешения "Invalid read array from JSON".

    // Вариант "Без пустого конструктора, поля можно делать финальными, с аннотациями на полный конструктор @JsonCreator и @JsonProperty"
    // Видимо более предпочтительный.
    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;

    // Аннотации Jackson: https://nsergey.com/jackson-annotations/
    @JsonCreator
    public MealTo(@JsonProperty("id") Integer id,
                  @JsonProperty("dateTime") LocalDateTime dateTime,
                  @JsonProperty("description") String description,
                  @JsonProperty("calories") int calories,
                  @JsonProperty("excess") boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }


/*    // Вариант "С пустым конструктором, не финальными полями"
    private Integer id;
    private LocalDateTime dateTime;
    private String description;
    private int calories;
    private boolean excess;

    public MealTo() {}

    public MealTo(Integer id,
                  LocalDateTime dateTime,
                  String description,
                  int calories,
                  boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }
*/

    public Integer getId() {
        return id;
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
