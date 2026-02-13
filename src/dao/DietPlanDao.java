package dao;

import model.DietPlan;

public interface DietPlanDao {

    /**
     * Salvează un plan de dietă în baza de date.
     */
    void save(DietPlan plan);

    /**
     * Returnează ultimul plan de dietă creat pentru userId.
     */
    DietPlan findLastByUserId(int userId);
}