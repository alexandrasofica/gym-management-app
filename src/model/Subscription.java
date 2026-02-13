package model;

import java.time.LocalDate;
/**
 * Clasa reprezintă un abonament al utilizatorului
 */
public class Subscription {

    private int id;
    private int userId;
    private SubscriptionType type;
    private UserType userType;
    private double basePrice;
    private double finalPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;  
    /**
     * Constructor fără parametrii
     */
    public Subscription() {}

    /**
     * Getter
     * @return id-ul obiectului curent
     */
    public int getId() {
        return id;
    }
    /**
     * Setter
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Getter
     * @return id-ul utilizatorului
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Setter
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Getter
     * @return tipul abonamentului
     */
    public SubscriptionType getType() {
        return type;
    }
    /**
     * Setter
     * @param type
     */
    public void setType(SubscriptionType type) {
        this.type = type;
    }
    /**
     * Getter
     * @return tipul utilizatorului
     */
    public UserType getUserType() {
        return userType;
    }
    /**
     * Setter
     * @param userType
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    /**
     * Getter
     * @return prețul inițial al abonamentului
     */
    public double getBasePrice() {
        return basePrice;
    }
    /**
     * Setter
     * @param basePrice
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    /**
     * Getter
     * @return prețul final (cu reducere)
     */
    public double getFinalPrice() {
        return finalPrice;
    }
    /**
     * Setter
     * @param finalPrice
     */
    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
    /**
     * Getter
     * @return data începerii abonamentului
     */
    public LocalDate getStartDate() {
        return startDate;
    }
    /**
     * Setter
     * @param startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    /**
     * Getter 
     * @return data expirării abonamentului
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    /**
     * Setter
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    /**
     * Getter
     * @return statusul abonamentului (ACTIV/EXPIRAT)
     */
    public String getStatus() {
        return status;
    }
    /**
     * Setter
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Metoda toString
     * afișarea unui obiect
     */
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", userType=" + userType +
                ", basePrice=" + basePrice +
                ", finalPrice=" + finalPrice +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
