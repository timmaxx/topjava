package ru.javawebinar.topjava.to;

import java.time.LocalDateTime;

public class MealTo {
    // Сделал пустой конструктор. А следовательно и поля сделал не final.
    // Это всё для разрешения "Invalid read array from JSON".
    // Или был другой путь?
    private /*final*/ Integer id;

    private /*final*/ LocalDateTime dateTime;

    private /*final*/ String description;

    private /*final*/ int calories;

    private /*final*/ boolean excess;

    public MealTo() {}

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

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
