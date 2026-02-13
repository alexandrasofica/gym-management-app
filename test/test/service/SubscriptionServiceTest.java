package test.service;

import dao.SubscriptionDao;
import model.Subscription;
import model.SubscriptionType;
import model.UserType;
import org.junit.jupiter.api.Test;
import service.SubscriptionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionServiceTest {

	/**
	 * verifică întreaga logică legată de gestionarea abonamentelor din aplicație
	 */
	
    private static class InMemorySubscriptionDao implements SubscriptionDao {
        private final List<Subscription> data = new ArrayList<>();

        @Override
        public void save(Subscription s) {
            if (s.getId() == 0) {
                s.setId(data.size() + 1);
            }
            data.add(s);
        }

        @Override
        public List<Subscription> findAll() {
            return new ArrayList<>(data);
        }

        @Override
        public List<Subscription> findByUserType(UserType userType) {
            List<Subscription> out = new ArrayList<>();
            for (Subscription s : data) {
                if (s.getUserType() == userType) out.add(s);
            }
            return out;
        }

        @Override
        public List<Subscription> findByType(SubscriptionType type) {
            List<Subscription> out = new ArrayList<>();
            for (Subscription s : data) {
                if (s.getType() == type) out.add(s);
            }
            return out;
        }

        @Override
        public void deleteByUserId(int userId) {
            data.removeIf(s -> s.getUserId() == userId);
        }

        // dacă interfața ta SubscriptionDao are și metoda asta, păstreaz-o:
        @Override
        public List<Subscription> findByStatus(String status) {
            List<Subscription> out = new ArrayList<>();
            for (Subscription s : data) {
                if (s.getStatus() != null && s.getStatus().equalsIgnoreCase(status)) out.add(s);
            }
            return out;
        }
    }

    @Test
    void createSubscription_student_applies30PercentDiscount_andSetsStatusActiv_andParsesDates() {
        InMemorySubscriptionDao dao = new InMemorySubscriptionDao();
        SubscriptionService service = new SubscriptionService(dao);

        service.createSubscription(
                12,
                SubscriptionType.LUNAR,
                UserType.STUDENT,
                100.0,
                "2025-12-08",
                "2026-01-08"
        );

        List<Subscription> all = dao.findAll();
        assertEquals(1, all.size());

        Subscription s = all.get(0);
        assertEquals(12, s.getUserId());
        assertEquals(SubscriptionType.LUNAR, s.getType());
        assertEquals(UserType.STUDENT, s.getUserType());

        // 30% reducere -> 70
        assertEquals(100.0, s.getBasePrice(), 0.0001);
        assertEquals(70.0, s.getFinalPrice(), 0.0001);

        assertNotNull(s.getStartDate());
        assertNotNull(s.getEndDate());
        assertEquals("ACTIV", s.getStatus());
    }

    @Test
    void createSubscription_angajat_applies50PercentDiscount() {
        InMemorySubscriptionDao dao = new InMemorySubscriptionDao();
        SubscriptionService service = new SubscriptionService(dao);

        service.createSubscription(
                1,
                SubscriptionType.ANUAL,
                UserType.ANGAJAT,
                200.0,
                "2025-1-1",
                "2026-1-1"
        );

        Subscription s = dao.findAll().get(0);

        // 50% reducere -> 100
        assertEquals(200.0, s.getBasePrice(), 0.0001);
        assertEquals(100.0, s.getFinalPrice(), 0.0001);
        assertEquals("ACTIV", s.getStatus());
    }

    @Test
    void createSubscription_normal_hasNoDiscount() {
        InMemorySubscriptionDao dao = new InMemorySubscriptionDao();
        SubscriptionService service = new SubscriptionService(dao);

        service.createSubscription(
                2,
                SubscriptionType.LUNAR,
                UserType.NORMAL,
                123.45,
                "2025-12-10",
                "2026-1-10"
        );

        Subscription s = dao.findAll().get(0);
        assertEquals(123.45, s.getFinalPrice(), 0.0001);
    }

    @Test
    void getAll_returnsSavedSubscriptions() {
        InMemorySubscriptionDao dao = new InMemorySubscriptionDao();
        SubscriptionService service = new SubscriptionService(dao);

        service.createSubscription(1, SubscriptionType.LUNAR, UserType.NORMAL, 100, "2025-12-01", "2026-01-01");
        service.createSubscription(2, SubscriptionType.ANUAL, UserType.STUDENT, 900, "2025-12-01", "2026-12-01");

        assertEquals(2, service.getAll().size());
    }

    @Test
    void deleteByUserId_removesOnlyThatUsersSubscriptions() {
        InMemorySubscriptionDao dao = new InMemorySubscriptionDao();
        SubscriptionService service = new SubscriptionService(dao);

        service.createSubscription(2, SubscriptionType.LUNAR, UserType.NORMAL, 100, "2025-12-01", "2026-01-01");
        service.createSubscription(2, SubscriptionType.ANUAL, UserType.STUDENT, 900, "2025-12-01", "2026-12-01");
        service.createSubscription(3, SubscriptionType.LUNAR, UserType.NORMAL, 200, "2025-12-01", "2026-01-01");

        service.deleteByUserId(2);

        List<Subscription> remaining = service.getAll();
        assertEquals(1, remaining.size());
        assertEquals(3, remaining.get(0).getUserId());
    }

    @Test
    void createSubscription_invalidDate_throwsException() {
        InMemorySubscriptionDao dao = new InMemorySubscriptionDao();
        SubscriptionService service = new SubscriptionService(dao);

        assertThrows(Exception.class, () ->
                service.createSubscription(
                        1, SubscriptionType.LUNAR, UserType.NORMAL, 100,
                        "invalid-date",
                        "2026-01-01"
                )
        );
    }
}
