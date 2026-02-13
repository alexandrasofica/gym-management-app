package service;

import dao.SubscriptionDao;
import model.Subscription;
import model.SubscriptionType;
import model.UserType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * Clasa gestionează logica abonamentelor
 */
public class SubscriptionService {

    private final SubscriptionDao subscriptionDao;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-M-d");
    /**
     * Constructor
     * @param subscriptionDao
     */
    public SubscriptionService(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    /**
     * Verifică dacă userul are deja un abonament ACTIV în baza de date
     * @param userId
     * @return
     */
    public boolean hasActiveSubscription(int userId) {
        return getAll().stream()
                .anyMatch(s -> s.getUserId() == userId && "ACTIV".equalsIgnoreCase(s.getStatus()));
    }
    /**
     * Creează un abonament nou
     * se aplică discount-ul
     * calculează prețul final
     * @param userId
     * @param type
     * @param userType
     * @param basePrice
     * @param startDateStr
     * @param endDateStr
     */

    public void createSubscription(int userId, SubscriptionType type, UserType userType,
                                   double basePrice, String startDateStr, String endDateStr) {

        if (hasActiveSubscription(userId)) {
            throw new RuntimeException("Aveți deja un abonament activ! Nu puteți crea altul până nu expiră cel actual.");
        }

        double discount = 0.0;
        if (userType == UserType.STUDENT) {
            discount = 0.30;  
        } else if (userType == UserType.ANGAJAT) {
            discount = 0.50;   
        }

        double finalPrice = basePrice * (1 - discount);
        LocalDate startDate = LocalDate.parse(startDateStr.trim(), DATE_FMT);
        LocalDate endDate   = LocalDate.parse(endDateStr.trim(), DATE_FMT);

        Subscription s = new Subscription();
        s.setUserId(userId);
        s.setType(type);
        s.setUserType(userType);
        s.setBasePrice(basePrice);
        s.setFinalPrice(finalPrice);
        s.setStartDate(startDate);
        s.setEndDate(endDate);
        s.setStatus("ACTIV");

        subscriptionDao.save(s);
    }
    /**
     * Getter
     * @return lista abonaților
     */
    public List<Subscription> getAll() { return subscriptionDao.findAll(); }
    public List<Subscription> getByUserType(UserType userType) { return subscriptionDao.findByUserType(userType); }
    public List<Subscription> getByType(SubscriptionType type) { return subscriptionDao.findByType(type); }
    public void deleteByUserId(int userId) { subscriptionDao.deleteByUserId(userId); }
}