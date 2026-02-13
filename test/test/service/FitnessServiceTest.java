package test.service;

import dao.FitnessGoalDao;
import model.FitnessGoal;
import org.junit.jupiter.api.Test;
import service.AiPlanService;
import service.FitnessService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste unitare pentru clasa FitnessService.
 * 
 * Testele verifică logica de salvare și recuperare
 * a obiectivelor fitness, fără a apela AI real.
 */
public class FitnessServiceTest {

    /**
     * Implementare fake (în memorie) a FitnessGoalDao.
     * 
     * Este folosită doar pentru teste,
     * astfel încât să nu fie nevoie de baza de date.
     */
    static class InMemoryFitnessGoalDao implements FitnessGoalDao {

        /**
         * Listă care simulează tabela fitness_goals
         */
        private final List<FitnessGoal> data = new ArrayList<>();

        /**
         * Salvează un obiectiv fitness în listă
         * @param goal obiectivul fitness
         */
        @Override
        public void save(FitnessGoal goal) {
            data.add(goal);
        }

        /**
         * Returnează cel mai recent obiectiv fitness
         * pentru un anumit user
         * @param userId id-ul utilizatorului
         * @return ultimul obiectiv fitness sau null
         */
        @Override
        public FitnessGoal findLastByUserId(int userId) {
            for (int i = data.size() - 1; i >= 0; i--) {
                if (data.get(i).getUserId() == userId) {
                    return data.get(i);
                }
            }
            return null;
        }
    }

    /**
     * Testează dacă FitnessService:
     * salvează corect obiectivele
     * returnează întotdeauna ultimul obiectiv salvat
     */
    @Test
    void saveGoal_thenGetLastGoalForUser_returnsLatestGoal() {

        var dao = new InMemoryFitnessGoalDao();

        var service = new FitnessService(dao, new AiPlanService());

        service.saveGoal(new FitnessGoal(
                1, 80, 75, 180,
                "SLABIRE",
                "MODERAT",
                "PRIMUL PLAN"
        ));

        service.saveGoal(new FitnessGoal(
                1, 80, 70, 180,
                "SLABIRE",
                "MODERAT",
                "AL DOILEA PLAN"
        ));


        FitnessGoal last = service.getLastGoalForUser(1);

        assertNotNull(last, "Obiectivul returnat nu trebuie să fie null");
        assertEquals("AL DOILEA PLAN", last.getNotes(),
                "Trebuie returnat ultimul obiectiv salvat");
    }
}
