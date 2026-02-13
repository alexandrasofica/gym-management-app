package model;
/**
 * Clasa reprezintă o rezervare făcută de utilizator 
 */
import java.io.Serializable;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String date; 
    private String time; 
    private String note; 


    public Reservation() {}
    /**
     * Constructor
     * @param userId
     * @param date
     * @param time
     * @param note
     */
    public Reservation(int userId, String date, String time, String note) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.note = note;
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
     * @return id-ul rezervării
     */
    public int getUserId() { return userId; }
    /**
     * Setter
     * @param userId
     */
    public void setUserId(int userId) { this.userId = userId; }
    /**
     * Getter
     * @return data rezervării
     */
    public String getDate() { return date; }
    /**
     * Setter
     * @param date
     */
    public void setDate(String date) { this.date = date; }
    /**
     * Getter
     * @return ora la care se face rezervarea
     */
    public String getTime() { return time; }
    /**
     * Setter
     * @param time
     */
    public void setTime(String time) { this.time = time; }
    /**
     * Getter
     * @return specificațiile
     */
    public String getNote() { return note; }
    /**
     * Setter
     * @param note
     */
    public void setNote(String note) { this.note = note; }
}
