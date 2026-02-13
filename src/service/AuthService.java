package service;

import dao.UserDao;
import model.User;
/**
 * Clasa se ocupă de autentificarea utilizatorilor
 */
public class AuthService {

    private UserDao userDao;
    /**
     * Constructor
     * @param userDao
     */
    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }
    /**
     * verifică daca email-ul și parola sunt corecte
     * decide daca user-ul are voie sau nu sa intre in aplicatie
     * @param email
     * @param password
     * @return true/false
     */
    public boolean login(String email, String password) {
        System.out.println("Login cu: " + email + " / " + password);

        User user = userDao.findByEmail(email);
        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);
    }
	/**
	 * Verifică datele de autentificare
	 * @param email
	 * @param password
	 * @return user dacă datele sunt corecte, null altfel
	 */
    public User authenticate(String email, String password) {
        User user = userDao.findByEmail(email); 
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("User logat: " + user.getEmail() + " cu rolul: " + user.getRole());
            return user;
        }
        return null;
    }
}
