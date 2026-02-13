/**
 * 
 */
package app;

import dao.UserDaoJdbcImpl;
import dao.SubscriptionDaoJdbcImpl;
import service.AuthService;
import service.SubscriptionService;
import ui.RoleSelectionFrame;
import javax.swing.SwingUtilities;
/**
 * clasa Main porneșe efectiv aplicația
 */
public class Main {
    /**
     * Funcția pregătește baza de date și pornește pagina de selecție a rolului
     * @param args
     */
    public static void main(String[] args) {
        UserDaoJdbcImpl userDao = new UserDaoJdbcImpl();
        SubscriptionDaoJdbcImpl subscriptionDao = new SubscriptionDaoJdbcImpl();
        userDao.createDefaultAdminIfMissing();
        AuthService authService = new AuthService(userDao);
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionDao);

        SwingUtilities.invokeLater(() -> {
            new RoleSelectionFrame(authService, subscriptionService, userDao, "").setVisible(true);
        });
    }
}