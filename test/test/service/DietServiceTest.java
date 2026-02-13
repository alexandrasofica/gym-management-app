package test.service;

import dao.DietPlanDao;
import model.DietPlan;
import org.junit.jupiter.api.Test;
import service.DietService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DietServiceTest {
	/**
	 * pentru logica de salvare a planurilor de dietă
	 */
    private static class LocalDietPlanDao implements DietPlanDao {
        private final List<DietPlan> data = new ArrayList<>();

        @Override
        public void save(DietPlan plan) { data.add(plan); }

        @Override
        public DietPlan findLastByUserId(int userId) {
            for (int i = data.size() - 1; i >= 0; i--) {
                if (data.get(i).getUserId() == userId) return data.get(i);
            }
            return null;
        }
    }
    /**
     * se verifică daca intotdeauna se returneaza cel mai recent plan de dieta
     */
    @Test
    void savePlan_thenLastPlanForUser_returnsMostRecent() {
        var dao = new LocalDietPlanDao();
        var service = new DietService(dao);

        service.savePlan(new DietPlan(1, 0, "PLAN 1"));
        service.savePlan(new DietPlan(1, 0, "PLAN 2"));

        DietPlan last = service.getLastPlanForUser(1);
        assertNotNull(last);
        assertEquals("PLAN 2", last.getPlanText());
    }
}
