package dao;
/**
 * Permite gestionarea completă a abonamentelor
 * salvare
 * ștergere
 * filtrare
 */
import java.util.List;
import model.Subscription;
import model.SubscriptionType;
import model.UserType;

public interface SubscriptionDao {
	
	/**
	 * adaugă user nou
	 * @param subscription
	 */
    void save(Subscription subscription);
    /**
     * modifică informațiile unui user existent
     * @param userId
     */
    void deleteByUserId(int userId);

    /**
     * Metoda vizualizare
     * @return toate abonamentele
     */
    List<Subscription> findAll();
    /**
     * Metoda filtrare dupa tipul abonamentului
     * @param type
     * @return abonamentele de un anumit tip
     */
    List<Subscription> findByType(SubscriptionType type);
    /**
     * Metod filtrare dupa tipul utilizatorului
     * @param userType
     * @return abonamentele in functie de tipul utilizatorului
     */
    List<Subscription> findByUserType(UserType userType);
    /**
     * Metoda pentru abonament ACTIV/EXPIRTA
     * @param status
     * @return
     */
    List<Subscription> findByStatus(String status); 
}
