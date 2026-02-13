package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role; 

    public User() {}
    /**
     * Constructor
     * @param id
     * @param name
     * @param email
     * @param password
     * @param role
     */
    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    /**
     * Getter
     * @return id-ul utilizatorului
     */
    public int getId() { return id; }
    /**
     * Setter
     * @param id
     */
    public void setId(int id) { this.id = id; }
    /**
     * Getter
     * @return numele utilizatorului
     */
    public String getName() { return name; }
    /**
     * Setter
     * @param name
     */
    public void setName(String name) { this.name = name; }
    /**
     * Getter
     * @return email-ul utilizatorului
     */
    public String getEmail() { return email; }
    /**
     * Setter
     * @param email
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Getter
     * @return parola utilizatorului
     */
    public String getPassword() { return password; }
    /**
     * Setter
     * @param password
     */
    public void setPassword(String password) { this.password = password; }
    /**
     * Getter
     * @return rolul (admin/user)
     */
    public String getRole() { return role; }
    /**
     * Setter
     * @param role
     */
    public void setRole(String role) { this.role = role; }
}
