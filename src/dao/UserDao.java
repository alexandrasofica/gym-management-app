package dao;
/**
 * Permite gestionarea completă a utilizatorilor din baza de date 
 */
import model.User;

public interface UserDao {
	/**
	 * salvează user nou
	 * @param user
	 */
    void save(User user);
    /**
     * Cauta user prim email
     * @param email
     * @return user-ul găsit
     */
    User findByEmail(String email);
    /**
     * @param id
     * @return user prin ID
     */
    User findById(int id); 
    void createDefaultAdminIfMissing(); 
    void update(User user);

}
