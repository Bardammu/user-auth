package com.globallogic.userauth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

/**
 * Entity that represents a phone number
 *
 * @since 1.0.0
 */
@Entity
public class Phone {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Phone number should not be null")
    private long number;

    @NotNull(message = "Phone number city code should not be null")
    private int citycode;

    @NotNull(message = "Phone number country code should not be null")
    private String countrycode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getCitycode() {
        return citycode;
    }

    public void setCitycode(int citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
