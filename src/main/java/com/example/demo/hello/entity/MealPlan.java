package com.example.demo.hello.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_plan")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_plan_id")
    private Long mealPlanId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "part_of_day")
    private String partOfDay;

    @OneToMany(mappedBy = "mealPlan")
    @JsonManagedReference
    @JsonIgnoreProperties("mealPlan")
    private List<MealPlanEntry> mealPlanEntries = new ArrayList<>();

    public MealPlan() {}

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getPartOfDay() {
        return partOfDay;
    }

    public void setPartOfDay(String partOfDay) {
        this.partOfDay = partOfDay;
    }

    public List<MealPlanEntry> getMealPlanEntries() {
        return mealPlanEntries;
    }

    public void setMealPlanEntries(List<MealPlanEntry> mealPlanEntries) {
        this.mealPlanEntries = mealPlanEntries;
    }

    @Override
    public String toString() {
        return (
            "MealPlan{" +
            "mealPlanId=" +
            mealPlanId +
            ", userId=" +
            (user != null ? user.getUserId() : null) +
            ", dayOfWeek='" +
            dayOfWeek +
            '\'' +
            ", partOfDay='" +
            partOfDay +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealPlan)) return false;
        MealPlan that = (MealPlan) o;
        return mealPlanId != null && mealPlanId.equals(that.mealPlanId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
