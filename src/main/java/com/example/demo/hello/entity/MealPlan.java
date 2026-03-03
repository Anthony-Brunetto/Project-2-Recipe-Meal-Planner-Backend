package com.example.demo.hello.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mealPlan")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealPlanId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return (
            "MealPlan{" +
            "mealPlanId=" +
            mealPlanId +
            ", userId=" +
            (user != null ? user.getUserId() : null) +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
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
